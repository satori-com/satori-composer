package com.satori.composer.vertx;

import io.vertx.core.http.*;

public class ExtHttpHeaders {
  public final static CharSequence VERTX_AGENT = HttpHeaders.createOptimized("vertx");
  public final static CharSequence VERTX_SRV = HttpHeaders.createOptimized("vertx");
  public final static CharSequence KEEPALIVE_CON = HttpHeaders.createOptimized("keep-alive");
  public final static CharSequence NO_CACHE = HttpHeaders.createOptimized("no-cache");
}
