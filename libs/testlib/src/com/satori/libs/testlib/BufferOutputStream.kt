package com.satori.libs.testlib

import io.vertx.core.buffer.*
import java.io.*

class BufferOutputStream(var buffer: Buffer? = Buffer.buffer()) : OutputStream() {
  override fun write(b: ByteArray) {
    buffer?.appendBytes(b)
  }
  
  override fun write(b: ByteArray, off: Int, len: Int) {
    buffer?.appendBytes(b, off, len)
  }
  
  override fun write(b: Int) {
    buffer?.appendByte(b.toByte())
  }
  
  override fun close() {
    buffer = null
  }
}
