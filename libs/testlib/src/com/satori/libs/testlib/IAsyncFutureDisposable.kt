package com.satori.libs.testlib

import com.satori.libs.async.api.*

interface IAsyncFutureDisposable<T> : IAsyncFuture<T> {
  fun dispose()
}
