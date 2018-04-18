package com.satori.libs.vertx.kotlin

import com.satori.libs.common.kotlin.json.*
import io.vertx.core.json.*

object VxJsonContext : IJsonContext {
  override var mapper = Json.mapper
}