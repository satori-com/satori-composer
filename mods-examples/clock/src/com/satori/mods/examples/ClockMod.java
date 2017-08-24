package com.satori.mods.examples;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.suite.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class ClockMod extends Mod {
  public final static Logger log = LoggerFactory.getLogger(ClockMod.class);
  public final static long defaultTick = 3_000;
  
  private long tick = defaultTick;
  
  public ClockMod() {
    this(defaultTick);
  }
  
  public ClockMod(JsonNode settings) throws Exception {
    this(settings != null ? Config.parseAndValidate(settings, ClockModSettings.class) : null);
  }
  
  public ClockMod(ClockModSettings settings) {
    if (settings != null) {
      tick = settings.tick;
    }
    log.info("created with tick {}", tick);
  }
  
  public ClockMod(long tick) {
    this.tick = tick;
  }
  
  
  @Override
  public void init(IModContext context) throws Exception {
    super.init(context);
    // Post loop to the queue instead of making a direct call.
    // This ensures that loop() will be executed when all startup is done
    // and all dependent mods are ready
    exec(this::loop);
  }
  
  private void loop() {
    try {
      long msec = System.currentTimeMillis();
      log.info("yield tick {} (next in {} msec)", msec, tick);
      //report output data
      yield(msec, AsyncPromise.from(
        () -> {
          // successful completion
          timer(tick, this::loop);
        },
        cause -> {
          // error completion
          log.error("failure", cause);
        }
      ));
    } catch (Exception e) {
      log.error("failure", e);
    }
  }
}

