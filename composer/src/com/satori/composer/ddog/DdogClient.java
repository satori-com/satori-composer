package com.satori.composer.ddog;

import com.satori.async.api.*;
import com.satori.async.core.*;
import com.satori.composer.vertx.*;

import java.nio.charset.*;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.*;
import com.fasterxml.jackson.module.afterburner.*;
import io.netty.handler.codec.http.*;
import io.vertx.core.*;
import io.vertx.core.buffer.*;
import io.vertx.core.http.*;
import io.vertx.core.http.HttpHeaders;


public class DdogClient {
  HttpClient http;
  DdogClientConfig config;
  public final ObjectMapper mapper = new ObjectMapper()
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());
  
  
  public DdogClient(Vertx vertx, DdogClientConfig config) {
    http = vertx.createHttpClient(new HttpClientOptions()
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
    this.config = config;
  }
  
  private HttpClientRequest createPostRequest(String seg) {
    QueryStringEncoder qenc = new QueryStringEncoder(config.path + seg);
    for (HashMap.Entry<String, String> e : config.args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    String path = qenc.toString();
    HttpClientRequest request = http.post(path);
    for (HashMap.Entry<String, String> e : config.headers.entrySet()) {
      request.putHeader(e.getKey(), e.getValue());
    }
    request.putHeader(HttpHeaders.CONTENT_TYPE, HttpContentTypes.APP_JSON_UTF8);
    request.putHeader(HttpHeaders.USER_AGENT, ExtHttpHeaders.VERTX_AGENT);
    return request;
  }
  
  private HttpClientRequest setSink(HttpClientRequest request, IAsyncPromise<DdogApiResult> promise) {
    return request.handler(httpResponse -> {
      int statusCode = httpResponse.statusCode();
      String statusMessage = httpResponse.statusMessage();
      httpResponse.bodyHandler(buf -> {
        if (statusCode < 200 || statusCode >= 300) {
          String msg = String.format(
            "request (%s) failed  with %d '%s'",
            request.uri(), statusCode, statusMessage
          );
          promise.fail(new Exception(msg));
          return;
        }
        final DdogApiResult result;
        try {
          result = new DdogApiResult(
            statusCode, statusMessage, buf.toString(StandardCharsets.UTF_8)
          );
        } catch (Throwable cause) {
          promise.fail(cause);
          return;
        }
        promise.succeed(result);
      })
        .exceptionHandler(cause -> promise.fail(cause));
    })
      .exceptionHandler(cause -> promise.fail(cause));
  }
  
  public void postSeries(DdogSeries[] series, IAsyncHandler<DdogApiResult> cont) {
    final HttpClientRequest request;
    final IAsyncPromise<DdogApiResult> promise = AsyncPromise.from(cont);
    try {
      request = createPostRequest("series");
      setSink(request, promise);
      request.end(Buffer.buffer(
        mapper.writeValueAsBytes(new PostSeriesPayload(series))
      ));
    } catch (Throwable cause) {
      promise.fail(cause);
    }
    
  }
  
  public void postEvent(DdogEvent event, IAsyncHandler<DdogApiResult> cont) {
    final HttpClientRequest request;
    final IAsyncPromise<DdogApiResult> promise = AsyncPromise.from(cont);
    try {
      request = createPostRequest("events");
      setSink(request, promise);
      request.end(Buffer.buffer(
        mapper.writeValueAsBytes(event)
      ));
    } catch (Throwable cause) {
      promise.fail(cause);
    }
    
  }
  
  public void close() {
    if (http != null) {
      http.close();
    }
  }
  
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  public static class PostSeriesPayload {
    @JsonProperty("series")
    public DdogSeries[] series = null;
    
    public PostSeriesPayload(DdogSeries[] series) {
      this.series = series;
    }
  }
}
