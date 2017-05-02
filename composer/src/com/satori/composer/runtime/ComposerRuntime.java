package com.satori.composer.runtime;

import com.satori.mods.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.resources.*;
import com.satori.mods.suite.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import io.netty.util.*;
import io.vertx.core.*;
import io.vertx.core.impl.*;
import org.slf4j.*;

public class ComposerRuntime {
  public static final Logger log = LoggerFactory.getLogger(ComposerRuntime.class);
  public static final String defaultRuntimeConfigPath = "config.json";
  public static final String CONFIG_PATH_PROP = "mod.config";
  public static final String CONFIG_PATH_ENV = "MOD_CONFIG";
  
  // public methods
  
  public static String resolveConfigPath(/*CommandLine cmd*/) {
    String result;
    
    // TODO: add option to specify mod config path in command line arguments?
    /*result = cmd.getOptionValue("modConfigPath", null);
    if (result != null && !result.isEmpty()) {
      return result;
    }*/
    
    result = System.getProperty(CONFIG_PATH_PROP);
    if (result != null && !result.isEmpty()) {
      return result;
    }
    
    result = System.getenv(CONFIG_PATH_ENV);
    if (result != null && !result.isEmpty()) {
      return result;
    }
    
    return defaultRuntimeConfigPath;
  }
  
  public static ComposerRuntimeConfig loadConfig(String configPath) throws Exception {
    try (InputStream stream = ModResourceLoader.loadAsStream(configPath)) {
      return Config.parse(stream, ComposerRuntimeConfig.class);
    }
  }
  
  public static ComposerRuntimeConfig loadConfig() throws Exception {
    String configPath = resolveConfigPath();
    return loadConfig(configPath);
  }
  
  public static IMod createMod(String className, JsonNode config) {
    
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
  
  public static Vertx start(ComposerRuntimeConfig config) throws Exception {
    return start(new Composition(config.mods), config);
  }
  
  public static Vertx start(IMod mod, ComposerRuntimeConfig config) throws Exception {
    final Vertx vertx;
    vertx = createVertx();
    vertx.exceptionHandler(cause -> {
      log.error("terminating framework", cause);
      vertx.close();
    });
    
    vertx.deployVerticle(new ModVerticle(config, mod), ar -> {
      if (!ar.succeeded()) {
        log.error("failed to deploy mod verticle", ar.cause());
        vertx.close();
        return;
      }
      log.info("mod verticle deployed: deploymentId={}", ar.result());
    });
    return vertx;
  }
  
  public static Vertx start(IMod mod) throws Exception {
    ComposerRuntimeConfig config = new ComposerRuntimeConfig();
    config.validate();
    return start(mod, config);
  }
  
  // private methods
  
  private static Vertx createVertx() {
    VertxOptions vertxOpts = new VertxOptions();
    return Vertx.vertx(vertxOpts);
  }
  
  // entry point
  
  public static void prepare() throws Exception {
    System.setProperty(
      io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, "io.vertx.core.logging.SLF4JLogDelegateFactory"
    );
    System.setProperty(
      FileResolver.DISABLE_CP_RESOLVING_PROP_NAME, "true"
    );
    ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
  }
  
  public static void main(String... args) throws Exception {
    prepare();
    ComposerRuntimeConfig config = loadConfig();
    config.validate();
    start(config);
  }
}
