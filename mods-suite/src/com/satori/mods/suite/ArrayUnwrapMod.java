package com.satori.mods.suite;

import com.satori.mods.core.async.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.slf4j.*;

public class ArrayUnwrapMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(ArrayUnwrapMod.class);
  
  private final String path;
  
  public ArrayUnwrapMod() {
    this((String) null);
  }
  
  public ArrayUnwrapMod(JsonNode settings) {
    this(settings.textValue());
  }
  
  public ArrayUnwrapMod(String path) {
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
  
  @Override
  public IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    if (data != null && path != null && !path.isEmpty()) {
      data = data.at(path);
    }
    
    if (data == null || data.isNull() || data.isMissingNode()) {
      return AsyncResults.succeededNull;
    }
    
    if (!data.isArray()) {
      return yield(data);
    }
    
    ArrayNode array = (ArrayNode) data;
    if (array.size() <= 0) {
      return AsyncResults.succeededNull;
    } else if (array.size() == 1) {
      return yield(array.get(0));
    }
    
    ArrayDeque<JsonNode> deq = new ArrayDeque<>(array.size());
    for (JsonNode node : array) {
      deq.addLast(node);
    }
    AsyncFuture future = new AsyncFuture();
    yieldLoop(deq, future);
    return future;
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
        // log error if processing failed
        log.warn("failed to process array element", future.getError());
      }
    }
  }
}
