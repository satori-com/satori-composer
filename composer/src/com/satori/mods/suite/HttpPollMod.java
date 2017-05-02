package com.satori.mods.suite;

import com.satori.composer.vertx.*;
import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.netty.handler.codec.http.*;
import io.vertx.core.*;
import io.vertx.core.buffer.*;
import io.vertx.core.http.*;
import io.vertx.core.http.HttpHeaders;
import org.slf4j.*;

public class HttpPollMod extends Mod {
  public static final long INVALID_TIMER = Long.MIN_VALUE;
  public static final Logger log = LoggerFactory.getLogger(HttpPollMod.class);
  public static final ObjectMapper mapper = Config.mapper;
  
  public final HttpPollModStats stats = new HttpPollModStats();
  private HttpClient http = null;
  private final HttpPollModSettings config;
  private final String path;
  private Vertx vertx;
  private final long pollDelay;
  private final long errorDelay;
  private String lastEtag;
  private String lastModified;
  private final IBufferParser bufferParser;
  private final int maxRedirections = 10;
  private long timer = INVALID_TIMER;
  
  public enum State {
    stopped, idle, polling, stopping
  }
  
  private State state = State.stopped;
  
  public HttpPollMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, HttpPollModSettings.class));
  }
  
  public HttpPollMod(HttpPollModSettings config) throws Exception {
    this.config = config;
    QueryStringEncoder qenc = new QueryStringEncoder(config.path);
    for (HashMap.Entry<String, String> e : config.args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    path = qenc.toString();
    pollDelay = config.delay;
    errorDelay = config.errorDelay;
    if (config.format == null || config.format.isEmpty()) {
      bufferParser = this::processJsonContent;
    } else switch (config.format.toLowerCase()) {
      case "binary":
        bufferParser = this::processBinaryContent;
        break;
      case "json":
        bufferParser = this::processJsonContent;
        break;
      case "text":
        bufferParser = this::processTextContent;
        break;
      default:
        throw new RuntimeException("unsupported data format: " + config.format);
    }
    log.info("http polling mod created ({})", path);
  }
  
  // IComposerRuntimeModule implementation
  
  @Override
  public void init(IModContext context) throws Exception {
    super.init(context);
    this.vertx = ((Verticle) context.runtime()).getVertx();
    
    this.http = createHttpClient();
    switch (state) {
      case stopped:
        state = State.idle;
      case idle:
        break;
      case stopping:
        state = State.polling;
        break;
      default:
        throw new RuntimeException("invalid state");
    }
    lastEtag = null;
    lastModified = null;
    stats.reset();
    log.info("http polling module initialized ({})", path);
  }
  
  @Override
  public void onStart() throws Exception {
    doPolling();
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    Vertx vertx = this.vertx;
    this.vertx = null;
    
    if (http != null) {
      http.close();
      http = null;
    }
    
    lastEtag = null;
    lastModified = null;
    if (timer != INVALID_TIMER) {
      if (state != State.idle) {
        log.error("timer scheduled in invalid state: {}", state);
      }
      vertx.cancelTimer(timer);
      timer = INVALID_TIMER;
    }
    switch (state) {
      case idle:
        state = State.stopped;
        break;
      case polling:
        state = State.stopping;
        break;
      default:
        log.error("invalid state", new Exception());
        break;
    }
    stats.reset();
    log.info("http polling module stopped ({})", path);
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    stats.drain(collector);
  }
  
  @Override
  public void onPulse() {
    log.debug("pulse received");
  }
  
  // private methods
  
  private void doPolling() {
    // sanity check
    if (state != State.idle) {
      log.error("try polling in '{}' state", state);
      return;
    }
    state = State.polling;
    
    log.info("polling...");
    stats.pollInit += 1;
    
    if (http == null) {
      log.error("poll failed", new Exception("http client closed"));
      onPollCompleted(false);
      return;
    }
    try {
      sendRequest(path, 0, AsyncPromise.from(this::processResult));
    } catch (Exception cause) {
      log.error("poll failed", cause);
      onPollCompleted(false);
    }
    
  }
  
  public void processResult(IAsyncResult<JsonNode> ar) {
    if (ar.isFailed()) {
      log.warn("poll failed", ar.getError());
      stats.pollFail += 1;
      onPollCompleted(false);
      return;
    }
    JsonNode content = ar.getValue();
    if (content == null) {
      // not modified
      log.info("idle poll");
      stats.pollIdle += 1;
      onPollCompleted(true);
      return;
    }
    stats.sent += 1;
    IAsyncPromise promise = AsyncPromise.from(this::onMessageConsumed);
    try {
      yield(content, promise);
    } catch (Exception cause) {
      promise.fail(cause);
    }
  }
  
  private void onMessageConsumed(IAsyncResult ar) {
    if (ar.isFailed()) {
      log.warn("processing message error", ar.getError());
    }
    onPollCompleted(ar.isSucceeded());
  }
  
  private void onPollCompleted(boolean ok) {
    final long delay = ok ? pollDelay : errorDelay;
    switch (state) {
      case polling:
        state = State.idle;
        timer = vertx.setTimer(delay, ar -> {
          timer = INVALID_TIMER;
          doPolling();
        });
        break;
      case stopping:
        state = State.stopped;
        break;
      default:
        log.error("invalid state", new Exception());
        break;
    }
  }
  
  protected void sendRequest(String path, int redirectCnt, IAsyncPromise<JsonNode> promise) {
    if (redirectCnt > maxRedirections) {
      promise.fail("too many redirection");
      return;
    }
    try {
      HttpClientRequest request = redirectCnt > 0 ? http.getAbs(path) : http.get(path);
      setRequestHeaders(request);
      request.exceptionHandler(promise::fail);
      request.handler(res -> processResponse(path, redirectCnt, res, promise));
      log.info("{} {}", request.method(), path);
      request.end();
    } catch (Throwable e) {
      promise.fail(e);
    }
  }
  
  protected void setRequestHeaders(HttpClientRequest request) {
    if (config.headers != null) {
      for (HashMap.Entry<String, String> e : config.headers.entrySet()) {
        String key = e.getKey();
        String val = e.getValue();
        if (val == null || val.isEmpty()) {
          continue;
        }
        request.putHeader(key, val);
      }
    }
    
    /*if(request.headers().get(HttpHeaders.ACCEPT)==null){
      switch (format){
        case json:
          request.putHeader(HttpHeaders.ACCEPT, HttpContentTypes.APP_JSON_UTF8);
          break;
        case protobuf:
          request.putHeader(HttpHeaders.ACCEPT, HttpContentTypes.APP_PROTOBUF);
          break;
      }
    }*/
    
    if (request.headers().get(HttpHeaders.CACHE_CONTROL) == null) {
      request.putHeader(HttpHeaders.CACHE_CONTROL, ExtHttpHeaders.NO_CACHE);
    }
    if (request.headers().get(HttpHeaders.USER_AGENT) == null) {
      request.putHeader(HttpHeaders.USER_AGENT, ExtHttpHeaders.VERTX_AGENT);
    }
    if (!config.disableEtag && lastEtag != null && !lastEtag.isEmpty()) {
      request.putHeader(HttpHeaders.IF_NONE_MATCH, lastEtag);
    }
    if (!config.disableLastModified && lastModified != null && !lastModified.isEmpty()) {
      request.putHeader(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
    }
  }
  
  protected void processResponse(String path, int redirectCnt, HttpClientResponse res, IAsyncPromise<JsonNode> promise) {
    res.exceptionHandler(promise::fail);
    res.bodyHandler(
      buf -> processResponseBody(path, redirectCnt, res, buf, promise)
    );
  }
  
  protected void processResponseBody(String path, int redirectCnt, HttpClientResponse response, Buffer buf, IAsyncPromise<JsonNode> promise) {
    final JsonNode result;
    try {
      int statusCode = response.statusCode();
      String statusMessage = response.statusMessage();
      
      if (statusCode == 302 | statusCode == 301) {
        String location = response.getHeader(HttpHeaders.LOCATION);
        log.info("{}, {} ({})", statusCode, statusMessage, location);
        sendRequest(location, redirectCnt + 1, promise);
        return;
      }
      
      log.info("{}, {} ({})", statusCode, statusMessage, path);
      
      if (statusCode == 304) {
        // not modified
        promise.succeed(null);
        return;
      }
      
      if (statusCode < 200 || statusCode >= 300) {
        promise.fail(String.format(
          "request (%s) failed  with %d '%s'",
          path, statusCode, statusMessage
        ));
        return;
      }
      
      String etag = response.getHeader(HttpHeaders.ETAG);
      if (etag != null && !etag.isEmpty()) {
        lastEtag = etag;
      }
      
      String modified = response.getHeader(HttpHeaders.LAST_MODIFIED);
      if (modified != null && !modified.isEmpty()) {
        lastModified = modified;
      }
      result = bufferParser.parse(buf);
    } catch (Throwable cause) {
      promise.fail(cause);
      return;
    }
    promise.succeed(result);
  }
  
  private JsonNode processJsonContent(Buffer buf) throws Exception {
    return mapper.readTree(buf.getBytes());
  }
  
  private JsonNode processTextContent(Buffer buf) throws Exception {
    return TextNode.valueOf(buf.toString());
  }
  
  private JsonNode processBinaryContent(Buffer buf) throws Exception {
    return mapper.getNodeFactory().binaryNode(buf.getBytes());
  }
  
  private HttpClient createHttpClient() {
    return vertx.createHttpClient(new HttpClientOptions()
      .setTryUseCompression(config.compression)
      .setMaxPoolSize(config.maxPoolSize)
      .setIdleTimeout(config.idleTimeout)
      .setSsl(config.ssl)
      .setDefaultHost(config.host)
      .setDefaultPort(config.port)
      .setKeepAlive(true)
      .setPipelining(true)
      .setMaxWaitQueueSize(config.maxWaitQueueSize)
      .setConnectTimeout(config.connectTimeout)
      .setVerifyHost(config.verifyHost)
    );
  }
  
  
  public interface IBufferParser {
    JsonNode parse(Buffer buf) throws Exception;
  }
}
