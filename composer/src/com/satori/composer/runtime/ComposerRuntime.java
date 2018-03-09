package com.satori.composer.runtime;

import com.satori.composer.config.loader.*;
import com.satori.mods.api.*;
import com.satori.mods.core.*;
import com.satori.mods.core.config.*;
import com.satori.mods.suite.*;

import com.fasterxml.jackson.databind.*;
import io.netty.util.*;
import io.vertx.core.*;
import io.vertx.core.impl.*;
import org.slf4j.*;

public class ComposerRuntime {
  private static final Logger log = LoggerFactory.getLogger(ComposerRuntime.class);
  
  /**
   * Starts composer with specified config loader.
   */
  public static Vertx start(ConfigLoader configLoader) throws Exception {
    return start(configLoader, DefaultModResolver.instance);
  }
  
  public static Vertx start(ConfigLoader configLoader, IModResolver modResolver) throws Exception {
    return start(configLoader.load(), modResolver);
  }
  
  public static Vertx start(JsonNode config) throws Exception {
    return start(config, DefaultModResolver.instance);
  }
  
  public static Vertx start(JsonNode config, IModResolver modResolver) throws Exception {
    return start(Config.parseAndValidate(config, ComposerRuntimeConfig.class), modResolver);
  }
  
  public static Vertx start(ComposerRuntimeConfig config) throws Exception {
    return start(config, DefaultModResolver.instance);
  }
  
  public static Vertx start(ComposerRuntimeConfig config, IModResolver modResolver) throws Exception {
    prepare();
    if (config == null) {
      throw new InvalidConfigException("Config must not be null");
    }
    config.validate();
    return start(new Composition(config.mods, modResolver), config);
  }
  
  /**
   * Main entry point to start composer.
   */
  private static Vertx start(IMod mod, ComposerRuntimeConfig config) throws Exception {
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
    prepare();
    
    ComposerRuntimeConfig config = new ComposerRuntimeConfig();
    config.validate();
    return start(mod, config);
  }
  
  private static Vertx createVertx() {
    VertxOptions vertxOpts = new VertxOptions()
      .setEventLoopPoolSize(1);
    return Vertx.vertx(vertxOpts);
  }
  
  public static void prepare() throws Exception {
    System.setProperty(
      io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
      io.vertx.core.logging.SLF4JLogDelegateFactory.class.getCanonicalName()
    );
    System.setProperty(
      FileResolver.DISABLE_CP_RESOLVING_PROP_NAME, "true"
    );
    ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
  }
}