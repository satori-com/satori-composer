package com.satori.mods.examples;

import com.satori.libs.async.core.*;
import com.satori.mods.api.*;
import com.satori.mods.suite.*;

import org.slf4j.*;

public class HelloWorldMod extends Mod {
  private final static Logger log = LoggerFactory.getLogger(HelloWorldMod.class);
  
  @Override
  public void init(IModContext context) throws Exception {
    super.init(context);
    exec(this::loop);
  }
  
  private void loop() {
    try {
      yield("Hello world", AsyncPromise.from(
        () -> { // success continuation
          loop();
        },
        cause -> { // failure continuation
          log.error("failure", cause);
        }
      ));
    } catch (Throwable e) {
      log.error("failure", e);
    }
  }
}

