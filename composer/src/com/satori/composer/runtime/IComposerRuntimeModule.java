package com.satori.composer.runtime;


import io.vertx.core.*;

public interface IComposerRuntimeModule extends IPulseObject {
  default void onStart(Vertx vertx) throws Exception {
  }
  
  default void onStop(Vertx vertx) throws Exception {
  }
}
