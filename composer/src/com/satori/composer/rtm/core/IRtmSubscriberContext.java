package com.satori.composer.rtm.core;

import com.fasterxml.jackson.databind.*;

public interface IRtmSubscriberContext extends IRtmChannelContext {
  
  default long subscribeTimeout() {
    return 3_000; // in ms, 3 sec.
  }
  
  default long subscriptionIdleTimeout() {
    return 300_000; // in ms, 5 min.
  }
  
  void onChannelData(JsonNode message);
  
  void onSubscribed(String next);
  
  void onUnsubscribed(String next);
  
  //void onError();
}
    