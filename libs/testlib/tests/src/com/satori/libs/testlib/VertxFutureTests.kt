package com.satori.libs.testlib

import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*
import java.util.concurrent.*

@RunWith(VertxUnitRunner::class)
class VertxFutureTests : VertxTest() {
  
  @Test
  fun threadTest(context: TestContext) = asyncTest(context) {
    val cpus = Runtime.getRuntime().availableProcessors()
    val ctx = vertx().getOrCreateContext()
    val started = timestamp()
    val afj = AsyncForkJoin()
    val threads = HashSet<Long>()
    for (i in 1..cpus * 20) afj.fork {
      val tid = thread {
        Thread.sleep(1000)
        return@thread Thread.currentThread().id
      }.await()
      assertSame(ctx, vertx().getOrCreateContext())
      threads.add(tid)
    }
    afj.join().await()
    val elapsed = timestamp() - started
    afj.lastError?.let { e -> throw e }
    assertTrue(elapsed in 800..1200)
    assertSame(ctx, vertx().getOrCreateContext())
    assertEquals(cpus * 20, threads.size)
  }
  
  @Test
  fun computeTest(context: TestContext) = asyncTest(context) {
    val cpus = ForkJoinPool.commonPool().parallelism
    val ctx = vertx().getOrCreateContext()
    val started = timestamp()
    val afj = AsyncForkJoin()
    val threads = HashSet<Long>()
    for (i in 1..cpus * 20) afj.fork {
      val tid = compute {
        Thread.sleep(100)
        return@compute Thread.currentThread().id
      }.await()
      assertSame(ctx, vertx().getOrCreateContext())
      threads.add(tid)
    }
    afj.join().await()
    val elapsed = timestamp() - started
    afj.lastError?.let { e -> throw e }
    assertTrue(elapsed in 1800..2200)
    assertSame(ctx, vertx().getOrCreateContext())
    assertEquals(ForkJoinPool.commonPool().parallelism, threads.size)
  }
  
  @Test
  fun yieldTest(context: TestContext) = asyncTest(context) {
    var cp = 0
    
    val f = future {
      assertEquals(0, cp++)
      
      yield()
      
      assertEquals(2, cp++)
    }
    
    assertFalse(f.isCompleted)
    assertEquals(1, cp++)
    
    f.await()
    
    assertTrue(f.isCompleted)
    assertEquals(3, cp++)
  }
  
  @Test
  fun timerTest(context: TestContext) = asyncTest(context) {
    val delay = 200L
    val started = System.currentTimeMillis()
    
    val f = timer(delay)
    
    assertFalse(f.isCompleted)
    
    val elapsed = f.await()
    
    assertEquals(0L, (delay - elapsed) / 20)
    assertEquals(0L, (delay - (System.currentTimeMillis() - started)) / 20)
  }
  
  @Test
  fun delayTest(context: TestContext) = asyncTest(context) {
    val delay = 200L
    var cp = 0
    
    val f = future {
      assertEquals(0, cp++)
      
      val elapsed = delay(delay)
      
      assertEquals(2, cp++)
      
      return@future elapsed
    }
    
    assertEquals(1, cp++)
    
    val elapsed = f.await()
    
    assertEquals(3, cp++)
    assertEquals(0L, (delay - elapsed) / 20)
  }
}
