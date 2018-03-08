package com.satori.libs.async.core

import com.satori.libs.testlib.*
import org.junit.*
import org.junit.Assert.*


class AyncQueueTests : Assert() {

  @Test
  fun genericTest() {

    val q = AsyncQueue<Int>()

    val f1 = q.deq()
    val f2 = q.deq()

    assertFalse(f1.isCompleted)
    assertFalse(f2.isCompleted)
    assertTrue(q.tryDeq() == null)

    q.enq(1)

    assertTrue(f1.isCompleted && f1.value == 1)
    assertFalse(f2.isCompleted)
    assertNull(q.tryDeq())

    q.enq(2)
    assertTrue(f2.isCompleted && f2.value == 2)
    assertNull(q.tryDeq())

    q.enq(3)
    q.enq(4)
    q.enq(5)

    assertTrue(q.tryDeq().result.get() == 3)

    val f4 = q.deq()
    assertTrue(f4.isCompleted && f4.value == 4)

    assertTrue(q.tryDeq().result.get() == 5)

    val f6 = q.deq()
    assertFalse(f6.isCompleted)
    assertNull(q.tryDeq())

    q.enq(6)
    assertTrue(f6.isCompleted && f6.value == 6)
    assertNull(q.tryDeq())
  }

  @Test
  fun deqTest() {
    var cp = 0

    val q = AsyncQueue<Int>()
    q.deq().onCompleted { ar: com.satori.libs.async.api.IAsyncResult<out Number> ->
      assertEquals(1, cp++)
      assertTrue(ar.get() == 1)
    }

    assertEquals(0, cp++)
    q.enq(1)
    assertEquals(2, cp++)

  }

  @Test
  fun tryDeqTest() {

    val q = AsyncQueue<String>()

    assertNull(q.tryDeq())

    q.enq("1")
    assertTrue("1" == q.tryDeq {
      assertTrue("1" == it.result.get())
      it.result.get()
    })
    assertNull(q.tryDeq())

    q.enq("2")

    assertTrue("2" == q.tryDeq().result.get())
    assertNull(q.tryDeq())

    q.fail(Exception("test"))

    q.tryDeq().let { future ->
      assertNotNull(future)
      assertTrue(future.isCompleted)
      assertFalse(future.result.isSucceeded)
      assertThrows(Exception::class.java) {
        future.result.get()
      }
    }

    AsyncFuture<String>().let { f ->
      q.promise(f)
      q.tryDeq().let { future ->
        assertNotNull(future)
        assertFalse(future.isCompleted)
        assertThrows(Exception::class.java) {
          future.result.get()
        }
      }
    }


    assertNull(q.tryDeq())

    assertNull(q.tryDeq { fail() })
    assertNull(q.tryDeq())
  }

  @Test
  fun tryEnqTest() {

    val q = AsyncQueue<Number>()

    assertNull(q.tryEnq())
    assertNull(q.tryEnq { fail() })

    q.enq(1)

    assertNull(q.tryEnq())
    assertNull(q.tryEnq { fail() })

    with(q.deq()) {
      assertTrue(isCompleted)
      assertTrue(1 == value)
    }

    q.deq().let { f ->
      assertFalse(f.isCompleted)
      assertSame(f, q.tryEnq())
      assertNull(q.tryEnq())
      q.enq(2)
      assertFalse(f.isCompleted)
      assertTrue(2 == q.tryDeq().result.get())
    }

    q.deq().let { f ->
      assertFalse(f.isCompleted)
      assertSame(f, q.tryEnq() {
        assertSame(it, f)
        it
      })
      assertNull(q.tryEnq())
      q.enq(2)
      assertFalse(f.isCompleted)
      assertTrue(2 == q.tryDeq().result.get())
    }

    q.promise().let{ promise->
      val f = q.deq()
      assertFalse(f.isCompleted)
      f.onCompleted {ar->
        assertTrue( 3 == ar.get())
      }
      promise.succeed(3)
    }
  }

  @Test
  fun awaitTest() {
    var cp = 0
    val q = AsyncQueue<Number>()

    val f = future(Unit) {
      assertEquals(0, cp++)

      assertTrue(1 == q.deq().await())

      return@future run {

        assertEquals(2, cp++)

        try {
          q.deq().await()
        } catch (ex: Throwable) {
          assertEquals(5, cp++)
        }
      }
    }

    assertEquals(1, cp++)
    assertFalse(f.isCompleted)

    q.enq(1)

    assertEquals(3, cp++)
    assertFalse(f.isCompleted)

    q.promise().let { promise ->
      assertEquals(4, cp++)
      assertFalse(f.isCompleted)

      promise.fail(Exception("test"))

      assertEquals(6, cp++)
      assertTrue(f.isCompleted)
    }

  }
}