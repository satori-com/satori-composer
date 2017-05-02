package com.satori.composer.rtm.core;


public interface IRtmPipelineHandler {
  void onStart(IRtmPipelineController master) throws Throwable;
  
  void onStop() throws Throwable;
  
  void onPulse(long timestamp) throws Throwable;
}
