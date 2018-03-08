package com.satori.libs.testlib

import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*

@RunWith(VertxUnitRunner::class)
class VertxFutureTests : VertxTest() {
  
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
