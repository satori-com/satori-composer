package com.satori.composer.vertx;

import io.vertx.core.http.*;

public class HttpContentTypes {
  public final static CharSequence TXT_PLAIN_UTF8 = HttpHeaders.createOptimized("text/plain; charset=utf-8");
  public final static CharSequence APP_JSON_UTF8 = HttpHeaders.createOptimized("application/json; charset=utf-8");
  public final static CharSequence APP_OCTET_STREAM = HttpHeaders.createOptimized("application/octet-stream");
  public final static CharSequence APP_PROTOBUF = HttpHeaders.createOptimized("application/x-protobuf");
}
