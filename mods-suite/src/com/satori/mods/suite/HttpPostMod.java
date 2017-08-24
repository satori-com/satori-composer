package com.satori.mods.suite;

import com.satori.composer.templates.*;
import com.satori.composer.vertx.*;
import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.*;
import com.fasterxml.jackson.module.afterburner.*;
import io.netty.handler.codec.http.*;
import io.vertx.core.*;
import io.vertx.core.buffer.*;
import io.vertx.core.http.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import org.slf4j.*;

public class HttpPostMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(HttpPostMod.class);
  public static final ObjectMapper mapper = new ObjectMapper()
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());
  
  
  private final HttpPostModStats stats = new HttpPostModStats();
  private final HttpPostModSettings config;
  private final String path;
  private final HttpMethod method;
  private final IJtTransform<JsonNode> template;
  private HttpClient http = null;
  
  public HttpPostMod(JsonNode config) throws Exception {
    this(Config.parseAndValidate(config, HttpPostModSettings.class));
  }
  
  public HttpPostMod(HttpPostModSettings config) throws Exception {
    this.config = config;
    QueryStringEncoder qenc = new QueryStringEncoder(config.path);
    for (HashMap.Entry<String, String> e : config.args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    path = qenc.toString();
    switch (config.method.toLowerCase()) {
      case "post":
        method = HttpMethod.POST;
        break;
      case "put":
        method = HttpMethod.PUT;
        break;
      default:
        throw new RuntimeException("unsupported method");
    }
    template = JtTransform.from(config.template);
    log.info("created");
  }
  
  // IBot implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    http = createHttpClient();
    stats.reset();
    log.info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    if (http != null) {
      http.close();
      http = null;
    }
    stats.reset();
    log.info("terminated");
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    log.debug("message received from: '{}'", inputName);
    stats.recv += 1;
    if (template != null) {
      data = template.transform(data);
    }
    sendRequest(data, AsyncPromise.from(ar -> {
      if (!ar.isSucceeded()) {
        cont.fail(ar.getError());
        stats.reqFailed += 1;
      } else {
        cont.succeed();
        stats.reqSucceeded += 1;
      }
    }));
  }
  
  // private methods
  
  private void sendRequest(JsonNode content, IAsyncPromise<byte[]> promise) {
    try {
      HttpClientRequest request = http.request(method, path);
      setRequestHeaders(request);
      request.exceptionHandler(promise::fail);
      request.handler(res -> processResponse(path, res, promise));
      log.debug("POST {}", path);
      request.end(Buffer.buffer(
        mapper.writeValueAsBytes(content)
      ));
    } catch (Throwable e) {
      promise.fail(e);
    }
  }
  
  private void setRequestHeaders(HttpClientRequest request) {
    if (config.headers != null) {
      for (HashMap.Entry<String, String> e : config.headers.entrySet()) {
        request.putHeader(e.getKey(), e.getValue());
      }
    }
    request.putHeader(HttpHeaders.CONTENT_TYPE, HttpContentTypes.APP_JSON_UTF8);
    request.putHeader(HttpHeaders.USER_AGENT, ExtHttpHeaders.VERTX_AGENT);
  }
  
  private void processResponse(String path, HttpClientResponse res, IAsyncPromise<byte[]> promise) {
    res.exceptionHandler(promise::fail);
    res.bodyHandler(
      buf -> processResponseBody(path, res, buf, promise)
    );
  }
  
  private void processResponseBody(String path, HttpClientResponse response, Buffer buf, IAsyncPromise<byte[]> promise) {
    int statusCode = response.statusCode();
    String statusMessage = response.statusMessage();
    if (statusCode < 200 || statusCode >= 300) {
      promise.fail(String.format(
        "request (%s) failed  with %d '%s'",
        path, statusCode, statusMessage
      ));
      return;
    }
    final byte[] result;
    try {
      result = buf.getBytes();
    } catch (Throwable cause) {
      promise.fail(cause);
      return;
    }
    promise.succeed(result);
  }
  
  private HttpClient createHttpClient() {
    return vertx().createHttpClient(new HttpClientOptions()
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
    );
  }
}
