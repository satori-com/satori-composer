package com.satori.libs.composition.drawer

import java.io.*
import java.nio.*
import kotlin.experimental.*

class ByteBufferInputStream(var buffer: ByteBuffer?) : InputStream() {
  
  override fun available(): Int {
    return buffer?.remaining() ?: 0
  }
  
  override fun read(): Int {
    val b = buffer ?: return -1
    return if (b.hasRemaining()) (b.get() and 0xFF.toByte()).toInt() else -1
  }
  
  override fun read(bytes: ByteArray, off: Int, len: Int): Int {
    val b = buffer ?: return -1
    if (!b.hasRemaining()) {
      return -1
    }
    val l = Math.min(len, b.remaining())
    b.get(bytes, off, l)
    return l
  }
  
  override fun close() {
    buffer = null
  }
}
