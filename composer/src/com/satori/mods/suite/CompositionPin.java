package com.satori.mods.suite;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;

public class CompositionPin implements IModOutput {
  public final ArrayList<IModInput> connectors;
  
  public CompositionPin() {
    this(new ArrayList<>());
  }
  
  public CompositionPin(ArrayList<IModInput> connectors) {
    this.connectors = connectors;
  }
  
  @Override
  public void yield(JsonNode data, IAsyncHandler cont) throws Exception {
    if (connectors.size() == 0) {
      cont.succeed();
      return;
    }
    if (connectors.size() == 1) {
      connectors.get(0).process(data, cont);
      return;
    }
    doYieldLoop(connectors.iterator(), data, cont);
  }
  
  private void doYieldLoop(Iterator<IModInput> itor, JsonNode msg, IAsyncHandler cont) throws Exception {
    IModInput h = itor.next();
    if (!itor.hasNext()) {
      h.process(msg, cont);
      return;
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
  }
  
  public void linkOutput(IModInput output) {
    connectors.add(output);
  }
  
  public void linkOutput(IMod mod, String inputName) {
    linkOutput((data, cont) -> {
      try {
        mod.onInput(inputName, data, cont);
      } catch (Throwable e) {
        cont.fail(e);
      }
    });
  }
  
  public void linkOutput(IMod mod) {
    linkOutput(mod, "default");
  }
}
