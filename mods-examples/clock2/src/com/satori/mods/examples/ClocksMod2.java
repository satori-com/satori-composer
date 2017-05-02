package com.satori.mods.examples;

import com.satori.mods.core.async.*;
import com.satori.mods.suite.*;

import org.slf4j.*;

public class ClocksMod2 extends Mod {
  public final static Logger log = LoggerFactory.getLogger(ClocksMod2.class);
  public final static long defaultDelay = 1000;
  
  private final long delay;
  
  public ClocksMod2() {
    this(defaultDelay);
  }
  
  public ClocksMod2(long delay) {
    this.delay = delay;
  }
  
  @Override
  public void onStart() {
    loop();
  }
  
  private void loop() {
    try {
      yield(System.currentTimeMillis(), AsyncPromise.from(
        () -> { // success continuation
          timer(delay, this::loop);
        },
        cause -> { // failure continuation
          log.error("failure", cause);
        }
      ));
    } catch (Exception e) {
      log.error("failure", e);
    }
  }
}

