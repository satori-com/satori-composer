package com.satori.libs.composition.drawer

import java.io.*
import java.nio.*

class ByteBufferOutputStream(var buffer: ByteBuffer?) : OutputStream() {
  
  override fun write(b: Int) {
    buffer?.put(b.toByte())
  }
  
  override fun write(bytes: ByteArray, off: Int, len: Int) {
    buffer?.put(bytes, off, len)
  }
  
  override fun write(b: ByteArray) {
    buffer?.put(b)
  }
  
  override fun close() {
    buffer = null
  }
}
