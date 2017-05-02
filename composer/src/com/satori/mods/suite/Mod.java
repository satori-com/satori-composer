package com.satori.mods.suite;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;

import java.io.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class Mod implements IMod, IModContext {
  private IModContext context = null;
  
  // IMod implementation
  
  @Override
  public void init(IModContext context) throws Exception {
    if (this.context != null) {
      throw new Exception("already started");
    }
    this.context = context;
  }
  
  @Override
  public void dispose() throws Exception {
    this.context = null;
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    cont.fail("no inputs defined");
  }
  
  // IModContext implementation
  
  public void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    context.yield(data, cont);
  }
  
  @Override
  public IModRuntime runtime() {
    return context.runtime();
  }
  
  @Override
  public IModOutput output() {
    return context.output();
  }
  
  @Override
  public void exec(Runnable action) throws Exception {
    context.exec(action);
  }
  
  @Override
  public Closeable timer(long delay, Runnable action) {
    return context.timer(delay, action);
  }
  
  // auxiliary functions
  
  public IModContext context() {
    return context;
  }
  
  public IAsyncFuture yield(JsonNode data) throws Exception {
    AsyncFuture res = new AsyncFuture();
    yield(data, res);
    return res;
  }
  
  public void yield(String data, IAsyncHandler cont) throws Exception {
    yield(TextNode.valueOf(data), cont);
  }
  
  public void yield(int data, IAsyncHandler cont) throws Exception {
    yield(IntNode.valueOf(data), cont);
  }
  
  public void yield(long data, IAsyncHandler cont) throws Exception {
    yield(LongNode.valueOf(data), cont);
  }
  
  public void yield(float data, IAsyncHandler cont) throws Exception {
    yield(FloatNode.valueOf(data), cont);
  }
  
  public void yield(double data, IAsyncHandler cont) throws Exception {
    yield(DoubleNode.valueOf(data), cont);
  }
  
  public void yield(Object data, IAsyncHandler cont) throws Exception {
    yield(Config.mapper.valueToTree(data), cont);
  }
}
