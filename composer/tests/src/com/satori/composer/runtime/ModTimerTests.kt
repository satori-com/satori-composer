package com.satori.composer.runtime

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.testlib.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*

@RunWith(VertxUnitRunner::class)
class ModTimerTests : VertxTest() {
  
  @Test
  fun `happy path test`(context: TestContext) = asyncTest(context) {
    val started = Stopwatch.timestamp()
    ModTimer(1000, vertx()).await()
    val elapsed = Stopwatch.timestamp() - started
    assertTrue(elapsed in 800..1200)
  }
  
  @Test
  fun `dispose earlier test`(context: TestContext) = asyncTest(context) {
    val started = Stopwatch.timestamp()
    val timer = ModTimer(10000, vertx())
    timer(1000).onCompleted {
      timer.dispose()
    }
    assertTrue(!timer.isCompleted)
    assertThrows<DisposedException> {
      timer.await()
    }
    val elapsed = Stopwatch.timestamp() - started
    assertTrue(elapsed in 800..1200)
  }
  
  @Test
  fun `dispose right away test`(context: TestContext) = asyncTest(context) {
    val started = Stopwatch.timestamp()
    val timer = ModTimer(10000, vertx())
    timer.dispose()
    assertThrows<DisposedException> {
      timer.await()
    }
    val elapsed = Stopwatch.timestamp() - started
    assertTrue(elapsed in 0..100)
  }
  
  @Test
  fun `dispose after test`(context: TestContext) = asyncTest(context) {
    val started = Stopwatch.timestamp()
    val timer = ModTimer(1000, vertx())
    timer.await()
    val elapsed = Stopwatch.timestamp() - started
    assertTrue(elapsed in 800..1200)
    timer.dispose()
  }
  
}
