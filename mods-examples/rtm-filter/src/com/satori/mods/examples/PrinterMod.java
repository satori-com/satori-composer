package com.satori.mods.examples;

import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.suite.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class PrinterMod extends Mod {
  public final static Logger log = LoggerFactory.getLogger(PrinterMod.class);
  
  public PrinterMod() {
  }
  
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    System.out.println(String.format(
      "%s: %s", inputName, Config.mapper.writeValueAsString(data)
    ));
  }
}

