package com.satori.mods.examples.suite;

import com.satori.mods.api.*;
import com.satori.mods.suite.*;

import java.util.*;

public class WellKnownMods implements IWellKnownMods {
  private final static HashMap<String, IModFactory> records;
  
  static {
    records = new HashMap<>();
    
    records.put("ex:delay", c -> new DelayMod());
    records.put("ex:clock", c -> new ClockMod());
  }
  
  // IWellKnownMods implementation
  
  @Override
  public IModFactory resolve(String shortName) {
    return records.get(shortName);
  }
}
