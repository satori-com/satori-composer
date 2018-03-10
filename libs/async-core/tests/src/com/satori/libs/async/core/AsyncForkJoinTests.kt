package com.satori.libs.async.core

import com.satori.libs.async.kotlin.*
import com.satori.libs.testlib.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*

@RunWith(VertxUnitRunner::class)
class AsyncForkJoinTests : VertxTest() {
  
  @Test
  fun `fork test`(context: TestContext) = asyncTest(context) {
    val started = timestamp()
    val afj = AsyncForkJoin()
    var cnt = 0
    for (i in 1..100) afj.fork {
      delay(i*10L)
      cnt += 1
    }
    afj.join().await()
    val elapsed = timestamp() - started
    afj.lastError?.let { e -> throw e }
    assertTrue(elapsed in 800..1200)
    assertEquals(100, cnt)
  }
  
  @Test
  fun `inc-dec test`(context: TestContext) = asyncTest(context) {
    val started = timestamp()
    val afj = AsyncForkJoin()
    var cnt = 0
    for (i in 1..100) {
      afj.inc()
      timer(i*10L).onCompleted{ar->
        cnt += 1
        afj.dec()
      }
    }
    afj.join().await()
    val elapsed = timestamp() - started
    afj.lastError?.let { e -> throw e }
    assertTrue(elapsed in 800..1200)
    assertEquals(100, cnt)
  }
  
  
}
