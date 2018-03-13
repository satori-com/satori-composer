package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;
import com.satori.mods.api.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class CompositionPin implements IModOutput {
  public final ArrayList<IModInput> connectors;
  public final static Logger log = LoggerFactory.getLogger(CompositionPin.class);
  
  public CompositionPin() {
    this(new ArrayList<>());
  }
  
  public CompositionPin(ArrayList<IModInput> connectors) {
    this.connectors = connectors;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    if (connectors.size() == 0) {
      cont.succeed();
      return;
    }
    if (connectors.size() == 1) {
      connectors.get(0).process(data, cont);
      return;
    }
    //doYieldLoop(connectors.iterator(), data, cont);
    doYieldLoop(connectors.iterator(), data).onCompleted(cont);
  }
  
  @Override
  public IAsyncFuture yield(JsonNode data) throws Exception {
    if (connectors.size() == 0) {
      return AsyncResults.succeededNull;
    }
    if (connectors.size() == 1) {
      return connectors.get(0).process(data);
    }
    return doYieldLoop(connectors.iterator(), data);
  }
  

  /*private void doYieldLoop(Iterator<IModInput> itor, JsonNode msg, IAsyncHandler cont) throws Exception {
    IModInput h = itor.next();
    if (!itor.hasNext()) {
      h.process(msg, cont);
    }
    h.process(msg, ar -> {
      if (!ar.isSucceeded()) {
        cont.fail(ar.getError());
        return;
      }
      try {
        doYieldLoop(itor, msg, cont);
      } catch (Throwable e) {
        cont.fail(e);
      }
    });
  }*/
  
  //deprecated
  /*public void doYieldLoop(Iterator<IModInput> itor, JsonNode msg, IAsyncHandler cont) throws Exception {
    while (true) {
      
      IModInput h = itor.next();
      if (!itor.hasNext()) {
        // this was the last element
        h.process(msg, cont);
        return;
      }
      
      IAsyncFuture<?> future = h.process(msg);
      if (!future.isCompleted()) {
        // operation still in progress, set continuation and exit
        future.onCompleted(ar -> {
          if (!ar.isSucceeded()) {
            cont.fail(ar.getError());
            return;
          }
          try {
            doYieldLoop(itor, msg, cont);
          } catch (Exception e) {
            cont.fail(e);
          }
        });
        return;
      }
      
      // operation was completed immediately
      IAsyncResult<?> ar = future.getResult();
      if (!ar.isSucceeded()) {
        // abort loop with failure
        cont.fail(ar.getError());
        return;
      }
    }
  }*/
  
  @SuppressWarnings("unchecked")
  public IAsyncFuture doYieldLoop(Iterator<IModInput> itor, JsonNode msg) throws Exception {
    AsyncForkJoin forkJoin = new AsyncForkJoin();
    while (itor.hasNext()) {
      IModInput input = itor.next();
      IAsyncFuture future;
      try {
        future = input.process(msg);
      } catch (Throwable e) {
        future = AsyncResults.failed(e);
        log.warn("branch failed", e);
      }
      forkJoin.inc();
      future.onCompleted(forkJoin);
    }
    forkJoin.dec();
    return forkJoin;
  }
  
  public void linkOutput(IModInput output) {
    connectors.add(output);
  }
  
  public void linkOutput(IMod mod, String inputName) {
    linkOutput((data, cont) -> mod.onInput(inputName, data, cont));
  }
  
  public void linkOutput(IMod mod) {
    linkOutput(mod, "default");
  }
}
