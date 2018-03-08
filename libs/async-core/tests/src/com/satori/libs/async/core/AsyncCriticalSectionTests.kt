package com.satori.libs.async.core

import com.satori.libs.testlib.*
import org.junit.*
import org.junit.Assert.*

class AsyncCriticalSectionTests : Assert() {
  
  @Test
  fun basicEnterLeaveTest() {
    
    var cp = 0
    val acs = AsyncCriticalSection()
    
    assertTrue(acs.tryEnter())
    assertFalse(acs.tryEnter())
    
    val f1 = future {
      assertEquals(0, cp++)
      acs.enter().await()
      assertEquals(4, cp++)
      acs.leave()
    }
    assertFalse(f1.isCompleted)
    
    assertEquals(1, cp++)
    
    val f2 = future {
      assertEquals(2, cp++)
      acs.enter().await()
      assertEquals(5, cp++)
      acs.leave()
    }
    assertFalse(f2.isCompleted)
    
    assertEquals(3, cp++)
    
    acs.leave()
    assertEquals(6, cp++)
    
    assertTrue(f1.isCompleted)
    assertTrue(f2.isCompleted)
  }
  
  @Test
  fun loopEnterLeaveTest() {
    
    var cp = 0
    val acs = AsyncCriticalSection()
    
    assertTrue(acs.tryEnter())
    
    val f1 = future {
      assertEquals(0, cp++)
      for (i in 1..1000_000) {
        acs.enter().await()
        acs.leave()
      }
    }
    
    val f2 = future {
      assertEquals(1, cp++)
      for (i in 1..1000_000) {
        acs.enter().await()
        acs.leave()
      }
    }
    
    assertFalse(f1.isCompleted)
    assertFalse(f2.isCompleted)
    
    assertEquals(2, cp++)
    
    acs.leave()
    
    assertTrue(f1.isCompleted)
    assertTrue(f2.isCompleted)
  }
  
  @Test
  fun loopExecTest() {
    
    val acs = AsyncCriticalSection()
    
    assertTrue(acs.tryEnter())
    
    fun loop(n: Int = 1000_000) {
      if (n >= 0) {
        acs.exec {
          loop(n - 1)
        }
      }
    }
    
    loop()
    acs.leave()
  }
  
  @Test
  fun exceptionExecTest() {
    var cp = 0
    val acs = AsyncCriticalSection{
      assertEquals(2, cp++)
    }
    
    assertTrue(acs.tryEnter())
    
    acs.exec { ar ->
      assertEquals(1, cp++)
      throw Exception("test")
    }
    
    acs.exec { ar ->
      assertEquals(3, cp++)
    }
    
    assertEquals(0, cp++)
  
    acs.leave()
    
    assertEquals(4, cp++)
  }
  
  @Test
  fun runTest() {
    var cp = 0
    val acs = AsyncCriticalSection()
    
    assertTrue(acs.tryEnter())
    
    val sig = AsyncFuture<Unit>()
    
    val f = future {
      assertEquals(0, cp++)
      return@future acs.run {
        assertEquals(2, cp++)
        sig.await()
        assertEquals(4, cp++)
        return@run "OK"
      }
    }
    assertTrue(!f.isCompleted)
    
    assertEquals(1, cp++)
    
    acs.leave()
    assertEquals(3, cp++)
    assertTrue(!f.isCompleted)
    
    sig.succeed(Unit)
    assertEquals(5, cp++)
    
    assertTrue(f.isCompleted)
    assertTrue(f.result.value == "OK")
  }
}
