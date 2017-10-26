package com.satori.mods.core;

import com.satori.mods.api.*;

import java.lang.reflect.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class ReflectionModFactory implements IModFactory {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Class modClass;
  
  @SuppressWarnings("WeakerAccess")
  public ReflectionModFactory(ClassLoader classLoader, String className) throws ClassNotFoundException {
    this(classLoader.loadClass(className).asSubclass(IMod.class));
  }
  
  @SuppressWarnings("WeakerAccess")
  public ReflectionModFactory(String className) throws ClassNotFoundException {
    this(Thread.currentThread().getContextClassLoader(), className);
  }
  
  @SuppressWarnings("WeakerAccess")
  public ReflectionModFactory(Class<? extends IMod> modClass) {
    this.modClass = modClass;
  }
  
  @Override
  public IMod create(JsonNode config) {
    
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
      RuntimeException err = new RuntimeException("matching constructors for mod not found");
      StringBuilder sb = new StringBuilder();
      boolean firstCtor = true;
      for (Constructor c : ctors) {
        if (firstCtor) {
          firstCtor = false;
        } else {
          sb.append(", ");
        }
        boolean firstParam = true;
        sb.append(Modifier.toString(c.getModifiers()));
        sb.append(' ');
        sb.append(modClass.getSimpleName());
        sb.append('(');
        for (Class cls : c.getParameterTypes()) {
          if (firstParam) {
            firstParam = false;
          } else {
            sb.append(", ");
          }
          sb.append(cls.getSimpleName());
        }
        sb.append(')');
      }
      log.error("no matching constructors found among: {} ", sb.toString());
      throw err;
    }
    
    try {
      return (IMod) defaultCtor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
