package com.satori.mods.suite;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.stats.*;

import java.io.*;

import com.fasterxml.jackson.databind.*;

public class CompositionNode implements IMod, IModContext {
  public final String name;
  public final IMod mod;
  public final CompositionPin outputPin;
  public IModContext context;
  
  
  public CompositionNode(String name, IMod mod) {
    this(name, mod, new CompositionPin());
  }
  
  public CompositionNode(String name, IMod mod, CompositionPin outputPin) {
    this.mod = mod;
    this.name = name;
    this.outputPin = outputPin;
  }
  
  @Override
  public IModRuntime runtime() {
    return context.runtime();
  }
  
  // IMod implementation
  
  @Override
  public void init(IModContext context) throws Exception {
    if (this.context != null) {
      throw new Exception("already started");
    }
    this.context = context;
    try {
      mod.init(this);
    } catch (Exception e) {
      this.context = null;
      throw e;
    }
  }
  
  @Override
  public void onStart() throws Exception {
    mod.onStart();
  }
  
  @Override
  public void onStop() throws Exception {
    mod.onStop();
  }
  
  @Override
  public void dispose() throws Exception {
    mod.dispose();
    this.context = null;
  }
  
  @Override
  public void onPulse() {
    mod.onPulse();
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    mod.onStats(cycle, collector.withPrefix(name));
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    mod.onInput(inputName, data, cont);
  }
  
  // IModContext implementation
  
  @Override
  public IModOutput output() {
    return this;
  }
  
  @Override
  public void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    outputPin.yield(data, cont);
  }
  
  @Override
  public void exec(Runnable action) throws Exception {
    context.exec(action);
  }
  
  @Override
  public Closeable timer(long delay, Runnable action) {
    return context.timer(delay, action);
  }
  
  public void linkOutput(IModInput output) {
    outputPin.linkOutput(output);
  }
  
  public void linkOutput(IMod mod, String inputName) {
    outputPin.linkOutput(mod, inputName);
  }
  
  public void linkOutput(IMod mod) {
    outputPin.linkOutput(mod);
  }
}