package com.satori.mods.examples;

import com.satori.composer.runtime.*;
import com.satori.mods.resources.*;

import java.io.*;
import java.util.logging.*;

public class RtmFilterApp {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntime.prepare();
    try (final InputStream is = ModResourceLoader.loadAsStream("log.properties")) {
      LogManager.getLogManager().readConfiguration(is);
    }
    ComposerRuntime.start(ModResourceLoader.loadAsConfigAndValidate(
      "config.json", ComposerRuntimeConfig.class
    ));
  
  }
}
