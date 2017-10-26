package com.satori.mods.core;

import com.satori.mods.api.*;

public class ModFactories {
  public static IModFactory from(Class<? extends IMod> modClass) {
    return new ReflectionModFactory(modClass);
  }
  
}
