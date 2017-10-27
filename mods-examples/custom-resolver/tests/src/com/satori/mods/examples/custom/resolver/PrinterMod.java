package com.satori.mods.examples.custom.resolver;

import com.satori.libs.async.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.suite.*;

import java.io.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class PrinterMod extends Mod {
  public final Logger log = LoggerFactory.getLogger(this.getClass());
  
  private final PrintStream printStream;
  private final String prefix;
  
  
  public PrinterMod(String prefix, PrintStream printStream) {
    this.printStream = printStream;
    this.prefix = prefix != null ? prefix : "";
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    onInput(inputName, data).onCompleted(cont);
  }
  
  @Override
  public IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    printStream.println(String.format(
      "%s[%s]: %s", prefix, inputName, Config.mapper.writeValueAsString(data)
    ));
    return AsyncResults.succeeded();
  }
}

