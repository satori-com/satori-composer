package com.satori.composer.rtm.core;

public class RtmStoppedState extends RtmState<IRtmContext, Void> {
  
  public RtmStoppedState(IRtmContext ctx) {
    super(ctx);
  }
  
  @Override
  public void start() {
    succeed(null);
  }
  
  @Override
  public boolean stopped() {
    return true;
  }
  
  @Override
  public void onPulse(long timestamp) {
    if (age(timestamp) > 10_000) {
      log().info("too long in stopped state");
    }
  }
}
