package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;

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
    if (path != null && !path.startsWith("/")) {
      path = "/" + path;
    }
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
  
  private void yieldLoop(ArrayDeque<JsonNode> deq, IAsyncHandler cont) {
    while (true) {
      
      JsonNode element = deq.pollFirst();
      if (deq.isEmpty()) {
        // this was the last element
        try {
          yield(element, cont);
        } catch (Throwable e) {
          cont.fail(e);
        }
        return;
      }
      
      final IAsyncFuture<?> future;
      try {
        future = yield(element);
      } catch (Throwable e) {
        // log error if processing failed
        log.warn("failed to process array element", e);
        continue;
      }
      
      if (!future.isCompleted()) {
        // operation still in progress, set continuation and exit
        future.onCompleted(ar -> {
          
          if (!ar.isSucceeded()) {
            // log error if processing failed
            log.warn("failed to process array element", ar.getError());
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
      IAsyncResult<?> ar = future.getResult();
      if (!ar.isSucceeded()) {
        // log error if processing failed
        log.warn("failed to process array element", ar.getError());
      }
    }
  }
}
