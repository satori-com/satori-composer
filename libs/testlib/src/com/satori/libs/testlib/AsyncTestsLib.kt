package com.satori.libs.testlib

import com.satori.libs.async.kotlin.*
import com.satori.libs.vertx.kotlin.*
import io.vertx.core.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.impl.*
import org.junit.*
import org.junit.Assert.*
import kotlin.coroutines.experimental.*

open class TestScope(val context: TestContext, vertx: Vertx) : VxFutureScope(vertx) {
  
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

fun asyncTest(test: suspend () -> Unit) = sync(test)

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
