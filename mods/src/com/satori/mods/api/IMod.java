package com.satori.mods.api;

import com.satori.mods.core.async.*;
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
}
