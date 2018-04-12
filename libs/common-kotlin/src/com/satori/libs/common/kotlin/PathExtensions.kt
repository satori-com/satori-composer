package com.satori.libs.common.kotlin

import java.nio.file.*
import kotlin.coroutines.experimental.*

inline fun Path.fileExtension(): String {
  val fileName = toFile().name
  val i = toFile().name.lastIndexOf('.')
  if (i > 0) {
    return fileName.substring(i + 1)
  }
  return fileName
}

inline fun Path.fileNameWithoutExt(): String {
  val fileName = toFile().name
  val i = toFile().name.lastIndexOf('.')
  if (i > 0) {
    return fileName.substring(0, i)
  }
  return fileName
}

fun <T, R> Iterable<T>.select(transform: (T) -> R): Iterable<R> = Iterable {
  buildIterator {
    for (i in this@select) {
      yield(transform(i))
    }
  }
}

fun <T, R> List<T>.select(transform: (T) -> R) = mapTo(ArrayList(size), transform)