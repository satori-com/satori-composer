package com.satori.composer.stats.http;

import com.satori.composer.config.*;
import com.satori.composer.stats.*;
import com.satori.composer.vertx.*;
import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;

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


public class StatsHttpForwarder extends StatsJsonAggregator implements IStatsForwarder {
  public static final Logger log = LoggerFactory.getLogger(StatsHttpForwarder.class);
  public static final ObjectMapper mapper = new ObjectMapper()
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());
  
  public final StatsHttpForwarderConfig config;
  public final HttpMethod method;
  public HttpClient http = null;
  public final long period;
  private long nextTs = Long.MAX_VALUE;
  private int noack = 0;
  
  
  public StatsHttpForwarder(StatsHttpForwarderConfig config) {
    super(config.prefix, config.ext);
    period = config.period;
    this.config = config;
    switch (config.method) {
      case "POST":
        method = HttpMethod.POST;
        break;
      case "PUT":
        method = HttpMethod.PUT;
        break;
      default:
        throw new RuntimeException("unsupportes http method");
    }
  }
  
  
  // IComposerRuntimeModule implementation
  
  
  @Override
  public void onStart(Vertx vertx) {
    http = createHttpClient(vertx, config);
    nextTs = Stopwatch.timestamp() + period;
    suppress();
  }
  
  @Override
  public void onStop(Vertx vertx) {
    if (http != null) {
      http.close();
      http = null;
    }
    nextTs = Long.MAX_VALUE;
    suppress();
  }
  
  @Override
  public void onPulse(long ts) {
    if (noack > 0 || ts < nextTs) {
      return;
    }
    nextTs = ts + period;
    if (!isDirty()) {
      return;
    }
    noack += 1;
    try {
      sendRequest(drainAsJsonMessage(), AsyncPromise.from(ar -> {
        noack -= 1;
        if (!ar.isSucceeded()) {
          log.warn("statistics skipped", ar.getError());
        }
      }));
    } catch (Exception cause) {
      noack -= 1;
      log.warn("statistics skipped", cause);
    }
  }
  
  // IStatsForwarder implementation
  
  @Override
  public void dispose() {
    
  }
  
  @Override
  public boolean disposed() {
    return false;
  }
  
  // other methods
  
  protected void sendRequest(StatsJsonMessage msg, IAsyncPromise<byte[]> promise) {
    try {
      QueryStringEncoder qenc = new QueryStringEncoder(config.path);
      for (HashMap.Entry<String, String> e : config.args.entrySet()) {
        qenc.addParam(e.getKey(), e.getValue());
      }
      String path = qenc.toString();
      
      HttpClientRequest request = http.request(method, path);
      setRequestHeaders(request);
      request.exceptionHandler(promise::fail);
      request.handler(res -> processResponse(path, res, promise));
      log.debug("{} {}", method, path);
      request.end(Buffer.buffer(
        mapper.writeValueAsBytes(msg)
      ));
    } catch (Throwable e) {
      promise.fail(e);
    }
  }
  
  protected void setRequestHeaders(HttpClientRequest request) {
    if (config.headers != null) {
      for (HashMap.Entry<String, String> e : config.headers.entrySet()) {
        request.putHeader(e.getKey(), e.getValue());
      }
    }
    request.putHeader(HttpHeaders.CONTENT_TYPE, HttpContentTypes.APP_JSON_UTF8);
    request.putHeader(HttpHeaders.USER_AGENT, ExtHttpHeaders.VERTX_AGENT);
  }
  
  protected void processResponse(String path, HttpClientResponse res, IAsyncPromise<byte[]> promise) {
    res.exceptionHandler(promise::fail);
    res.bodyHandler(
      buf -> processResponseBody(path, res, buf, promise)
    );
  }
  
  protected void processResponseBody(String path, HttpClientResponse response, Buffer buf, IAsyncPromise<byte[]> promise) {
    int statusCode = response.statusCode();
    String statusMessage = response.statusMessage();
    if (statusCode < 200 || statusCode >= 300) {
      promise.fail(new Exception(String.format(
        "request (%s) failed  with %d '%s'",
        path, statusCode, statusMessage
      )));
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
  
  public static HttpClient createHttpClient(Vertx vertx, HttpConnectionConfig config) {
    return vertx.createHttpClient(new HttpClientOptions()
      .setTryUseCompression(false)
      .setMaxPoolSize(1)
      .setIdleTimeout(600) //10 min., in sec.
      .setSsl(config.ssl)
      .setDefaultHost(config.host)
      .setDefaultPort(config.port)
      .setKeepAlive(true)
      .setPipelining(true)
      .setMaxWaitQueueSize(-1)
      .setConnectTimeout(60_000) //in ms, 1 min
    );
  }
}
