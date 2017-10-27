package com.satori.mods.api;

import com.satori.libs.async.api.*;

import java.io.Closeable;

import com.fasterxml.jackson.databind.*;
import io.vertx.core.*;

public interface IModContext extends IModOutput {
  
  Vertx vertx();
  
  default IModOutput output() {
    return null;
  }
  
  default void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    IModOutput output = output();
    if (output == null) {
      cont.fail(new Exception("not defined"));
      return;
    }
    output.yield(data, cont);
  }
  
  default IAsyncFuture yield(JsonNode data) throws Exception {
    IModOutput output = output();
    if (output == null) {
      return AsyncResults.failed(new Exception("not defined"));
    }
    return output.yield(data);
  }
  
  void exec(Runnable action) throws Exception;
  
  Closeable timer(long delay, Runnable action);
  
}
