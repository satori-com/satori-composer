package com.satori.mods.examples;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ClockModSettings extends Config {

  @JsonProperty("tick")
  public long tick = 3_000; // in msec

  @Override
  public void validate() throws InvalidConfigException {
    if (tick <= 0) {
      throw new InvalidConfigException("invalid delay");
    }
  }

  public ClockModSettings() {
  }
}
