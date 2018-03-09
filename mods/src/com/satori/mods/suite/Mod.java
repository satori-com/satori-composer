package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.mods.api.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.vertx.core.*;

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
    cont.fail(new Exception("no inputs defined"));
  }
  
  // IModContext implementation
  
  @Override
  public void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    context.yield(data, cont);
  }
  
  @Override
  public IAsyncFuture yield(JsonNode data) throws Exception {
    return context.yield(data);
  }
  
  
  @Override
  public Vertx vertx() {
    return context.vertx();
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
  public IAsyncFutureDisposable timer(long delay) {
    return context.timer(delay);
  }
  
  // auxiliary functions
  
  public IModContext context() {
    return context;
  }
  
  public IAsyncFuture yield(String data) throws Exception {
    return yield(TextNode.valueOf(data));
  }
  
  public IAsyncFuture yield(int data) throws Exception {
    return yield(IntNode.valueOf(data));
  }
  
  public IAsyncFuture yield(long data) throws Exception {
    return yield(LongNode.valueOf(data));
  }
  
  public IAsyncFuture yield(float data) throws Exception {
    return yield(FloatNode.valueOf(data));
  }
  
  public IAsyncFuture yield(double data) throws Exception {
    return yield(DoubleNode.valueOf(data));
  }
  
  public IAsyncFuture yield(Object data) throws Exception {
    return yield(Config.mapper.valueToTree(data));
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
