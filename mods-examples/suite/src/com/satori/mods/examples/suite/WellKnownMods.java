package com.satori.mods.examples.suite;

import com.satori.mods.suite.*;

import java.util.*;

public class WellKnownMods implements IWellKnownMods {
  private final static HashMap<String, String> records;
  
  static {
    records = new HashMap<>();
    
    records.put("ex:delay", DelayMod.class.getCanonicalName());
    records.put("ex:clock", ClockMod.class.getCanonicalName());
  }
  
  // IWellKnownMods implementation
  
  @Override
  public String resolve(String shortName) {
    return records.get(shortName);
  }
}
