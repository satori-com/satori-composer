package com.satori.mods.examples;

import com.satori.libs.async.core.*;
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
  
  @SuppressWarnings("unchecked")
  private void loop() {
    try {
      yield(System.currentTimeMillis(), AsyncPromise.from(
        () -> { // success continuation
          timer(delay).onCompleted(ar->{
            if(ar.isSucceeded()){
              loop();
            }
          });
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

