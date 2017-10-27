package com.satori.mods.api;

import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;

import com.fasterxml.jackson.databind.*;

public interface IModOutput {
  void yield(JsonNode data, IAsyncHandler cont) throws Exception;
  
  default IAsyncFuture yield(JsonNode data) throws Exception {
    AsyncFuture future = new AsyncFuture();
    yield(data, future);
    return future;
  }
}
