package com.satori.composer.rtm.core;

public interface IRtmPduController {
  <T> void send(RtmPdu<T> pdu);
  
  boolean isWritable();
  
  void fail(Throwable cause);
  
  void close();
}


