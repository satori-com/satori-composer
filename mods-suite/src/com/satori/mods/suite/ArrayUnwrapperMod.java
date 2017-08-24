package com.satori.mods.suite;

import com.satori.mods.core.async.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class ArrayUnwrapperMod extends Mod {
  
  private final String path;
  
  public ArrayUnwrapperMod() {
    this((String) null);
  }
  
  public ArrayUnwrapperMod(JsonNode settings) {
    this(settings.textValue());
  }
  
  public ArrayUnwrapperMod(String path) {
    this.path = path;
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    if (data != null && path != null && !path.isEmpty()) {
      data = data.at(path);
    }
    
    if (data == null || data.isNull() || data.isMissingNode()) {
      cont.succeed();
      return;
    }
    
    if (!data.isArray()) {
      yield(data, cont);
      return;
    }
    
    ArrayNode array = (ArrayNode) data;
    if (array.size() <= 0) {
      cont.succeed();
      return;
    } else if (array.size() == 1) {
      yield(array.get(0), cont);
      return;
    }
    
    ArrayDeque<JsonNode> deq = new ArrayDeque<>(array.size());
    for (JsonNode node : array) {
      deq.addLast(node);
    }
    
    yieldLoop(deq, cont);
  }
  
  // private methods
  
  public void yieldLoop(ArrayDeque<JsonNode> deq, IAsyncHandler cont) throws Exception {
    while (true) {
      
      JsonNode element = deq.pollFirst();
      if (deq.isEmpty()) {
        // this was the last element
        yield(element, cont);
        return;
      }
      
      IAsyncFuture<?> future = yield(element);
      if (!future.isCompleted()) {
        // operation still in progress, set continuation and exit
        future.onCompleted(ar -> {
          if (!ar.isSucceeded()) {
            cont.fail(ar.getError());
            return;
          }
          try {
            yieldLoop(deq, cont);
          } catch (Exception e) {
            cont.fail(e);
          }
        });
        return;
      }
      
      // operation was completed immediately
      if (future.isFailed()) {
        // abort loop with failure
        cont.fail(future.getError());
        return;
      }
    }
  }
}
