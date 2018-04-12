package com.satori.libs.common.kotlin

import java.nio.*
import java.nio.charset.*

fun ByteBuffer.putString(value: String) = putString(value, StandardCharsets.UTF_8.newEncoder())

fun ByteBuffer.putString(value: String, enc: CharsetEncoder) {
  enc.reset()
  var res = enc.encode(CharBuffer.wrap(value), this, true)
  if (res.isError()) {
    res.throwException()
  }
  res = enc.flush(this)
  if (res.isError()) {
    res.throwException()
  }
}

