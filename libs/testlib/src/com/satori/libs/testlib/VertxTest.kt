package com.satori.libs.testlib

import io.vertx.core.*
import io.vertx.ext.unit.junit.*
import org.junit.*

open class VertxTest {
  
  @Rule
  @JvmField
  val rule = RunTestOnContext {
    val vertx = Vertx.vertx(VertxOptions()
      .setEventLoopPoolSize(1)
    )
    vertx.exceptionHandler { ex ->
      println("error: $ex")
    }
  }

  fun vertx() = rule.vertx()
}
