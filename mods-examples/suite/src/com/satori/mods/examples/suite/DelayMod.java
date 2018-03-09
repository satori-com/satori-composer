package com.satori.mods.examples.suite;


import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;
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
  
  @SuppressWarnings("unchecked")
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    yield(data, AsyncPromise.from(
      () -> timer(delay).onCompleted(cont), // success continuation
      cont::fail // failure continuation
    ));
  }
  
}

