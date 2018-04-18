package com.satori.libs.vertx.kotlin

typealias VxBuffer = io.vertx.core.buffer.Buffer
typealias VxAsyncResult<T> = io.vertx.core.AsyncResult<T>
typealias VxHandler<T> = io.vertx.core.Handler<T>
typealias VxAsyncHandler<T> = io.vertx.core.Handler<VxAsyncResult<T>>
typealias VxFuture<T> = io.vertx.core.Future<T>
typealias VxFileSystem = io.vertx.core.file.FileSystem
