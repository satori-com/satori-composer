package com.satori.composer.rtm.core;

import com.satori.async.api.*;

import com.fasterxml.jackson.databind.*;

public interface IRtm {
  
  void start();
  
  void stop();
  
  boolean stopped();
  
  boolean publish(String channel, JsonNode msg);
  
  <T> boolean publish(String channel, T msg);
  
  boolean isConnected();
  
  boolean onConnected(IAsyncHandler<Boolean> cont);
  
  boolean isWritable();
  
  boolean onWritable(IAsyncHandler<Boolean> cont);
  
}
