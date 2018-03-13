package com.satori.composer.runtime

import com.fasterxml.jackson.databind.*
import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import com.satori.libs.testlib.*
import com.satori.mods.api.*
import org.junit.*

class ComposerRuntimeTests : Assert() {
  
  class BadMod() : IMod {
    override fun init(context: IModContext) {
      context.exec {throw RuntimeException("test")}
    }
    
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      throw RuntimeException("inputs are not defined")
    }
  }
  
  init{
    ComposerRuntime.prepare()
  }
  
  @Test(timeout = 10000)
  fun `framework closes on unhandled exception test`() = asyncTest{
    val f = AsyncFuture<Unit>()
    ComposerRuntime.onClosed = Runnable{
      f.succeed(Unit)
    }
    ComposerRuntime.start(BadMod())
    f.await()
  }
  
}
