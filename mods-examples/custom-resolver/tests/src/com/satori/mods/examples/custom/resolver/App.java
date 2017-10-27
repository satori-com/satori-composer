package com.satori.mods.examples.custom.resolver;

import com.satori.composer.runtime.*;
import com.satori.mods.api.*;
import com.satori.mods.core.*;
import com.satori.mods.resources.*;

import java.io.*;
import java.util.logging.*;

public class App {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntime.prepare();
    try (final InputStream is = ModResourceLoader.loadAsStream("log.properties")) {
      LogManager.getLogManager().readConfiguration(is);
    }
    IModResolver modResolver = modRef -> {
      if ("my:printer".equals(modRef)) {
        return c -> new PrinterMod(c.get("prefix").asText(), System.out);
      }
      return DefaultModResolver.instance.resolve(modRef);
    };
    //ClockMod m = null;
    ComposerRuntime.start(ModResourceLoader.loadAsConfigAndValidate(
      "config.json", ComposerRuntimeConfig.class
    ), modResolver);
  }
}

