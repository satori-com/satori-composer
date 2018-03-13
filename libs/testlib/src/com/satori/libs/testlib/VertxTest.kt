package com.satori.libs.testlib

import io.vertx.core.*
import io.vertx.core.impl.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import java.util.concurrent.*

open class VertxTest : Assert() {

  
  
  @Rule
  @JvmField
  val rule = RunTestOnContext {
    System.setProperty(
      io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
      io.vertx.core.logging.SLF4JLogDelegateFactory::class.java.canonicalName
    )
    System.setProperty(
      FileResolver.DISABLE_CP_RESOLVING_PROP_NAME, "true"
    )
  
    val vertx = Vertx.vertx(VertxOptions().apply {
      eventLoopPoolSize = 1
    })
    vertx.exceptionHandler { ex ->
      println("error: $ex")
      vertx.close()
    }
  }
  
  fun vertx() = rule.vertx()
  
  fun timestamp() = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())
}
