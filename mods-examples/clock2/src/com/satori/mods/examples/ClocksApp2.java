package com.satori.mods.examples;

import com.satori.composer.runtime.*;
import com.satori.mods.resources.*;
import com.satori.mods.suite.*;

import java.io.*;
import java.util.logging.*;

public class ClocksApp2 {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntime.prepare();
    try (final InputStream is = ModResourceLoader.loadAsStream("log.properties")) {
      LogManager.getLogManager().readConfiguration(is);
    }
    
    Composition composition = new Composition();
    
    CompositionNode clocksNode = composition.addMod("clocks", new ClocksMod2());
    clocksNode.linkOutput((data, cont) -> {
      System.out.println(data.toString());
      cont.succeed();
    });
    
    ComposerRuntime.start(composition);
  }
}

