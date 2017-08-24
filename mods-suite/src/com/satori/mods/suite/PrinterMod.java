package com.satori.mods.suite;

import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;

import com.fasterxml.jackson.databind.*;

public class PrinterMod extends Mod {
  private final ObjectWriter jsonWriter;
  
  public PrinterMod() {
    jsonWriter = Config.mapper.writerWithDefaultPrettyPrinter();
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    System.out.println("input '" + inputName + "':");
    jsonWriter.writeValue(System.out, data);
    System.out.println();
    cont.succeed();
  }
}
