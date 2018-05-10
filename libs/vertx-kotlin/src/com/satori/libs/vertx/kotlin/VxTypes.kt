package com.satori.libs.vertx.kotlin

typealias VxBuffer = io.vertx.core.buffer.Buffer
typealias VxAsyncResult<T> = io.vertx.core.AsyncResult<T>
typealias VxHandler<T> = io.vertx.core.Handler<T>
typealias VxFuture<T> = io.vertx.core.Future<T>
typealias VxFileSystem = io.vertx.core.file.FileSystem
typealias VxMessage<T> = io.vertx.core.eventbus.Message<T>
typealias VxAsyncHandler<T> = VxHandler<VxAsyncResult<T>>
typealias VxMessageHandler<T> = VxHandler<VxMessage<T>>
typealias VxMessageInterceptor<T> = java.util.function.Function<VxMessage<T>, VxFuture<VxMessage<T>>>
