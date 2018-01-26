package com.satori.libs.testlib

import com.satori.libs.async.api.*
import io.vertx.core.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.impl.*
import org.junit.*
import org.junit.Assert.*
import kotlin.coroutines.experimental.*

open class TestScope(val context: TestContext, vertx: Vertx) : VertxFutureScope(vertx) {
  
  fun fail() {
    context.fail()
  }
  
  fun fail(ex: Throwable) {
    context.fail(ex)
  }
  
  fun assertTrue(cond: Boolean) {
    context.assertTrue(cond)
  }
  
  fun assertFalse(cond: Boolean) {
    context.assertFalse(cond)
  }
  
  fun assertEquals(expected: Any?, actual: Any?) {
    context.assertEquals(expected, actual)
  }
  
  fun assertEquals(expected: Any?, actual: Any?, message: String) {
    context.assertEquals(expected, actual, message)
  }
  
  fun assertNull(expected: Any?) {
    context.assertNull(expected)
  }
  
  fun assertNotNull(expected: Any?) {
    context.assertNotNull(expected)
  }
  
}

inline fun <reified T> asyncTest(context: TestContext, noinline test: suspend TestScope.() -> T) {
  
  val op = context.async()
  val cont = object : Continuation<T> {
    override val context: CoroutineContext
      get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      op.complete()
    }
    
    override fun resumeWithException(exception: Throwable) {
      (context as TestContextImpl).failed(exception)
    }
  }
  test.startCoroutine(TestScope(context, Vertx.currentContext().owner()), cont)
}

inline fun <reified T> asyncTest(noinline test: suspend () -> T): T {
  
  val cont = object : Continuation<T> {
    val sync = Object()
    var completed = false
    var result: IAsyncResult<T>? = null
    
    override val context: CoroutineContext
      get() = EmptyCoroutineContext
    
    override fun resume(value: T) {
      synchronized(sync) {
        result = AsyncResults.succeeded(value)
        completed = true
        sync.notifyAll()
      }
    }
    
    override fun resumeWithException(exception: Throwable) {
      synchronized(sync) {
        result = AsyncResults.failed(exception)
        completed = true
        sync.notifyAll()
      }
    }
    
    fun wait(): T {
      synchronized(sync) {
        if (!completed) {
          sync.wait()
        }
      }
      return result!!.get()
    }
  }
  
  test.startCoroutine(cont)
  
  return cont.wait()
}

inline fun <reified T : Throwable> Assert.assertThrows(block: () -> Unit) {
  var thrown = false
  try {
    block()
  } catch (e: Throwable) {
    if (e !is T) throw e
    thrown = true
  }
  if (!thrown) {
    fail()
  }
}
