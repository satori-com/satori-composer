package com.satori.composer.rtm.core;

import io.vertx.core.http.*;

public interface IWsFrameController {
  void send(WebSocketFrame frame);
  
  boolean isWritable();
  
  void fail(Throwable cause);
  
  void close();
}