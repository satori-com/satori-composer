package com.satori.mods.suite

import com.fasterxml.jackson.databind.*
import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import com.satori.libs.common.kotlin.json.*
import com.satori.libs.testlib.*
import com.satori.libs.vertx.kotlin.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*
import org.slf4j.*
import kotlin.coroutines.experimental.intrinsics.*

@RunWith(VertxUnitRunner::class)
class CompositionTests : ModTest() {
  
  class DelayMod() : Mod() {
    val log = LoggerFactory.getLogger("delay-mod@${Integer.toHexString(System.identityHashCode(this))}")
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      onInput(inputName, data).onCompleted { ar ->
        if (ar.isSucceeded) cont.succeed() else cont.fail(ar.error)
      }
    }
    
    override fun onInput(inputName: String, data: JsonNode) = future(VxFutureScope(context().vertx(), log)) {
      log.info("input: $data")
      yield(data).await()
      delay(data.asLong())
    }
  }
  
  class DelayMod2() : Mod() {
    val log = LoggerFactory.getLogger("delay-mod@${Integer.toHexString(System.identityHashCode(this))}")
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      onInput(inputName, data).onCompleted { ar ->
        if (ar.isSucceeded) cont.succeed() else cont.fail(ar.error)
      }
    }
    
    override fun onInput(inputName: String, data: JsonNode) = future(VxFutureScope(context().vertx(), log)) {
      log.info("input: $data")
      delay(data.asLong())
      yield(data).await()
    }
  }
  
  class CrashingMod() : Mod() {
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      throw RuntimeException("test")
    }
    
    override fun onInput(inputName: String, data: JsonNode): IAsyncFuture<*> {
      throw RuntimeException("test")
    }
  }
  
  class ExprMod() : Mod() {
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      yield(data.asInt() / 10, cont)
    }
  }
  
  @Test(timeout = 4000)
  fun `crash test`(context: TestContext) = asyncTest(context) {
    val mod = Composition().apply {
      val delay = addMod("delay", DelayMod2())
      val crash = addMod("crash", CrashingMod())
      val expr = addMod("expr", ExprMod())
      linkModInput(delay, "default", "default")
      linkModInput(crash, "default", "default")
      linkModInput(expr, "default", "default")
      linkOutput("delay")
      linkOutput("crash")
      linkOutput("expr")
    }
    
    mod.init(this@CompositionTests)
    
    val started = Stopwatch.timestamp()
    
    mod.onInput("default", jsonNode(1000)).await()
    
    val elapsed = Stopwatch.timestamp() - started
    println("elapsed: $elapsed")
    assertTrue(elapsed in 600..1400)
    
    delay(1000)
    
    stopMod(mod)
    
    assertEquals(
      listOf(jsonNode(100), jsonNode(1000)),
      outQueue.toList()
    )
  }
  
  @Test
  fun `order test`(context: TestContext) = asyncTest(context) {
    val mod = Composition().apply {
      val delay1 = addMod("delay1", DelayMod())
      val delay2 = addMod("delay2", DelayMod())
      linkModInput(delay1, "default", "default")
      linkModInput(delay2, "default", "default")
      linkOutput("delay1")
      linkOutput("delay2")
    }
    
    mod.init(this@CompositionTests)
    
    val afj = AsyncForkJoin()
    afj.fork {
      mod.onInput("default", jsonNode(1000)).await()
    }
    afj.fork {
      suspendCoroutineOrReturn { cont ->
        mod.onInput("default", jsonNode(100)) { ar ->
          if (ar.isSucceeded) cont.resume(Unit) else cont.resumeWithException(ar.error)
        }
        return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
      }
    }
    afj.fork {
      mod.onInput("default", jsonNode(10)).await()
    }
    
    afj.join().await()
    
    stopMod(mod)
    
    assertEquals(
      listOf(
        jsonNode(1000), jsonNode(1000),
        jsonNode(100), jsonNode(100),
        jsonNode(10), jsonNode(10)
      ),
      outQueue.toList()
    )
    
  }
}
