package com.satori.composer.rtm.core;

import com.satori.async.api.*;

import io.vertx.core.http.*;

public interface IRtmFactoryContext extends IRtmContext {
  int defaultPort = 80;
  int defaultSslPort = 443;
  boolean defaultSsl = false;
  int defaultIdleTimeout = 60_000;
  int defaultConnectTimeout = 30_000;
  int defaultMaxFrameSize = 32 * 1024 * 1024;
  int defaultReconnectDelay = 5_000;
  
  String host();
  
  String path();
  
  CaseInsensitiveHeaders headers();
  
  default int port() {
    return ssl() ? defaultPort : defaultSslPort;
  }
  
  default boolean ssl() {
    return defaultSsl;
  }
  
  default int idleTimeout() {
    return defaultIdleTimeout;
  }
  
  default int connectTimeout() {
    return defaultConnectTimeout;
  }
  
  default int maxFrameSize() {
    return defaultMaxFrameSize;
  }
  
  default long reconnectDelay() {
    return defaultReconnectDelay;
  }
  
  void connect(IAsyncHandler<WebSocket> cont);
}
    