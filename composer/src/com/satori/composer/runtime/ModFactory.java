package com.satori.composer.runtime;

import com.satori.mods.api.*;
import com.satori.mods.suite.*;

import java.lang.reflect.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class ModFactory {
  private static final Logger log = LoggerFactory.getLogger(ModFactory.class);
  private static final ServiceLoader<IWellKnownMods> wellKnownMods = ServiceLoader.load(IWellKnownMods.class);
  
  public static String resolve(String className){
    if("composition".equals(className)){
      return Composition.class.getCanonicalName();
    }
    for(IWellKnownMods provider: wellKnownMods){
      String result = provider.resolve(className);
      if(result != null){
        return result;
      }
    }
    return className;
  }
  
  public static IMod create(String className, JsonNode config) {
    
    className = resolve(className);
    
    final Class<? extends IMod> modClass;
    try {
      modClass = Class.forName(className).asSubclass(IMod.class);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    
    Constructor<?>[] ctors = modClass.getDeclaredConstructors();
    Class[] configParams = new Class[]{JsonNode.class};
    Constructor defaultCtor = null;
    
    for (Constructor<?> ctor : ctors) {
      Class[] params = ctor.getParameterTypes();
      if (params.length == 0) {
        defaultCtor = ctor;
      } else if (Arrays.equals(params, configParams)) {
        try {
          return (IMod) ctor.newInstance(config);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
    }
    if (defaultCtor == null) {
      throw new RuntimeException("matching constructors for mod not found");
    }
    
    try {
      return (IMod) defaultCtor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}