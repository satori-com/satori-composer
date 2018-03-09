package com.satori.mods.suite

import com.fasterxml.jackson.databind.*
import com.satori.composer.runtime.*
import com.satori.libs.async.api.*
import com.satori.libs.async.api.IAsyncFutureDisposable
import com.satori.libs.testlib.*
import com.satori.mods.api.*
import java.util.*

open class ModTest : VertxTest(), IModContext, IModOutput {
  
  val outQueue = ArrayDeque<JsonNode>()
  
  override fun exec(action: Runnable) {
    vertx().runOnContext {
      action.run()
    }
  }
  
  override fun timer(delay: Long): IAsyncFutureDisposable<*> {
    return ModTimer(delay, vertx())
  }
  
  override fun output(): IModOutput {
    return this
  }
  
  override fun yield(data: JsonNode): IAsyncFuture<*> = future {
    outQueue.addLast(data)
  }
  
  override fun yield(data: JsonNode, cont: IAsyncHandler<*>) {
    outQueue.addLast(data)
    cont.succeed()
  }
  
  fun startMod(mod: IMod) {
    mod.init(this)
    mod.onStart()
  }
  
  fun stopMod(mod: IMod) {
    mod.onStop()
    mod.dispose()
  }
}
