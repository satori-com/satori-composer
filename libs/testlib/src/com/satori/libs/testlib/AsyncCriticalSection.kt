package com.satori.libs.testlib

import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import java.util.*

class AsyncCriticalSection {
  
  private val awaiters = ArrayDeque<IAsyncHandler<Unit>>()
  private var entered = false
  private var inloop = false
  
  fun enter(cont: IAsyncHandler<Unit>) {
    if (entered) {
      awaiters.addLast(cont)
      return
    }
    entered = true
    cont.succeed()
  }
  
  fun enter(): IAsyncFuture<Unit> {
    if (entered) {
      val f = AsyncFuture<Unit>()
      awaiters.addLast(f)
      return f
    }
    entered = true
    return AsyncResults.succeeded(Unit)
  }
  
  fun tryEnter(): Boolean {
    if (entered) {
      return false
    }
    entered = true
    return true
  }
  
  fun leave() {
    if (!entered) {
      throw IllegalStateException("trying to leave unentered critical section")
    }
    entered = false
    if (inloop) {
      return
    }
    inloop = true
    var error: Throwable? = null
    try {
      do {
        val cont = awaiters.pollFirst()
        if (cont === null) {
          return
        }
        entered = true
        try {
          cont.succeed()
        } catch (ex: Throwable) {
          error = ex // TODO: error can be overwritten
        }
      } while (!entered)
    } finally {
      inloop = false
      if (error != null) {
        throw error
      }
    }
  }
  
  fun exec(cont: IAsyncHandler<Unit>) {
    if (entered) {
      awaiters.addLast(IAsyncHandler { _ ->
        try {
          cont.succeed(Unit)
        } finally {
          leave()
        }
      })
      return
    }
    entered = true
    try {
      cont.succeed()
    } finally {
      leave()
    }
  }
}
