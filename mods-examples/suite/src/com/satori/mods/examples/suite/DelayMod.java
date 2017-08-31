package com.satori.mods.examples.suite;


import com.satori.mods.core.async.*;
import com.satori.mods.suite.*;

import com.fasterxml.jackson.databind.*;

public class DelayMod extends Mod {
  public final static long defaultDelay = 1000;
  
  private final long delay;
  
  public DelayMod() {
    this(defaultDelay);
  }
  
  public DelayMod(long delay) {
    this.delay = delay;
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    yield(data, AsyncPromise.from(
      () -> timer(delay, cont::succeed), // success continuation
      cont::fail // failure continuation
    ));
  }
  
}

