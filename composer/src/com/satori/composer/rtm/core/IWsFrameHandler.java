package com.satori.composer.rtm.core;

import io.vertx.core.http.*;

public interface IWsFrameHandler {
  void onRecv(WebSocketFrame frame) throws Throwable;
  
  void onWritableChanged(boolean isWritable) throws Throwable;
  
  void onStart(IWsFrameController master) throws Throwable;
  
  void onStop() throws Throwable;
  
  void onPulse(long timestamp) throws Throwable;
}