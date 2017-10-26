package com.satori.composer.runtime;

import com.satori.mods.api.*;
import com.satori.mods.core.*;
import com.satori.mods.suite.*;

import java.util.*;

import org.slf4j.*;

public class DefaultModResolver implements IModResolver {
  public static IModResolver instance = new DefaultModResolver();
  
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  protected static final ServiceLoader<IWellKnownMods> wellKnownMods = ServiceLoader.load(IWellKnownMods.class);
  
  @Override
  public IModFactory resolve(String modRef) throws Exception {
    if ("composition".equals(modRef)) {
      return Composition::new;
    }
    
    for (IWellKnownMods provider : wellKnownMods) {
      IModFactory result = provider.resolve(modRef);
      if (result != null) {
        return result;
      }
    }
    
    return new ReflectionModFactory(modRef);
  }
}