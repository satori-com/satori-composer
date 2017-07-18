package com.satori.mods.examples;

import com.satori.composer.runtime.*;

public class RtmFilterApp {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntimeConfig config = ComposerRuntime.loadConfig("config.json");
    config.validate();
    new ComposerRuntime().start(config);
  }
}
