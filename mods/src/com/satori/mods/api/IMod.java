package com.satori.mods.api;

import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;
import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.databind.*;

public interface IMod {
  
  default void init(IModContext context) throws Exception {
  }
  
  default void dispose() throws Exception {
  }
  
  default void onStart() throws Exception {
  }
  
  default void onStop() throws Exception {
  }
  
  default void onPulse() {
  }
  
  default void onStats(StatsCycle cycle, IStatsCollector collector) {
  }
  
  void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception;
  
  default IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    AsyncFuture future = new AsyncFuture();
    onInput(inputName, data, future);
    return future;
  }
}
