package com.satori.composer.rtm.core;


public class RtmRelaxingState extends RtmState<IRtmContext, RtmRelaxingState.Exit> {
  public final long delay;
  
  public RtmRelaxingState(IRtmContext ctx, long delay) {
    
    super(ctx);
    this.delay = delay;
  }
  
  @Override
  public boolean enter(Runnable exitHandler) {
    if (!super.enter(exitHandler)) {
      return false;
    }
    return true;
  }
  
  @Override
  public void onPulse(long timestamp) {
    if (checkNotCompleted() && age(timestamp) > delay) {
      succeed(Exit.started);
    }
  }
  
  @Override
  public void stop() {
    succeed(Exit.stopped);
  }
  
  public enum Exit {
    started, stopped
  }
  
}
