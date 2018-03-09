package com.satori.libs.testlib

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import org.junit.*


class FutureLibTests : Assert() {
  
  @Test
  fun onCompletedTest() {
    var cp = 0
    val q = AsyncQueue<Unit>()
    
    val f = future {
      assertEquals(0, cp++)
      q.deq().await()
      val f = future {
        assertEquals(2, cp++)
      }
      assertTrue(f.isCompleted)
      f.onCompleted {
        assertEquals(3, cp++)
      }
      f.await()
      assertEquals(4, cp++)
    }
    
    assertEquals(1, cp++)
    assertFalse(f.isCompleted)
    
    f.onCompleted {
      assertEquals(5, cp++)
    }
    q.enq(Unit)
    
    assertEquals(6, cp++)
    assertTrue(f.isCompleted)
    
    f.onCompleted {
      assertEquals(7, cp++)
    }
    
    assertEquals(8, cp++)
  }
}
