package com.satori.mods.examples;

import com.satori.composer.runtime.*;
import com.satori.mods.resources.*;
import com.satori.mods.suite.*;

import java.io.*;
import java.util.logging.*;

public class HelloWorldApp {
  
  // entry point
  
  public static void main(String... args) throws Exception {
    ComposerRuntime.prepare();
    try (final InputStream is = ModResourceLoader.loadAsStream("log.properties")) {
      LogManager.getLogManager().readConfiguration(is);
    }
    
    Composition composition = new Composition();
    
    CompositionNode helloWorldNode = composition.addMod("hello-world", new HelloWorldMod());
    CompositionNode delayNode = composition.addMod("delay", new DelayMod());
    CompositionNode publisherNode = composition.addMod("publisher", new RtmPublishMod(
      ModResourceLoader.loadAsJson("publisher.json")
    ));
    
    helloWorldNode.linkOutput(delayNode);
    delayNode.linkOutput(publisherNode);
    
    ComposerRuntime.start(composition);
  }
}

