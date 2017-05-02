package com.satori.composer.rtm.core;


import com.fasterxml.jackson.databind.*;

public interface IRtmPduHandler {
  void onStart(IRtmPduController master) throws Throwable;
  
  void onStop() throws Throwable;
  
  void onRecv(RtmPdu<JsonNode> pdu) throws Throwable;
  
  void onPulse(long timestamp) throws Throwable;
  
  void onWritableChanged(boolean isWritable) throws Throwable;
}
