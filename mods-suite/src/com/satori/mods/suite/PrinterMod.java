package com.satori.mods.suite;

import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;

import java.time.*;

import com.fasterxml.jackson.databind.*;

public class PrinterMod extends Mod {
  private final ObjectWriter jsonWriter;
  
  public PrinterMod() {
    jsonWriter = Config.mapper.writerWithDefaultPrettyPrinter();
  }
  
  // IMod implementation
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    processMessage(inputName, data);
    cont.succeed();
  }
  
  @Override
  public IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    processMessage(inputName, data);
    return AsyncResults.succeeded();
  }
  
  // private logic
  
  private void processMessage(String inputName, JsonNode data) throws Exception {
    System.out.print(String.format(
      "%s [%s]: ", Instant.now(), inputName
    ));
    jsonWriter.writeValue(System.out, data);
    System.out.println();
  }
}
