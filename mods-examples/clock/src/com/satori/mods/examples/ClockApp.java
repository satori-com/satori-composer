package com.satori.mods.examples;

import com.satori.composer.runtime.*;
import com.satori.mods.resources.*;

import java.io.*;
import java.util.logging.*;

public class ClockApp {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntime.prepare();
    try (final InputStream is = ModResourceLoader.loadAsStream("log.properties")) {
      LogManager.getLogManager().readConfiguration(is);
    }
    ComposerRuntimeConfig config = ComposerRuntime.loadConfig("config.json");
    config.validate();
    ComposerRuntime.start(config);
  }
}

