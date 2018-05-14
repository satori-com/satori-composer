package com.satori.libs.vertx.kotlin

import java.io.*
import kotlin.math.*

class VxBufferInputStream(var buffer: VxBuffer?) : InputStream() {
  var pos = 0
  
  override fun read(): Int {
    val buffer = this.buffer ?: throw Exception("stream closed")
    if (pos >= buffer.length()) return -1
    return buffer.getByte(pos++).toInt()
  }
  
  override fun read(b: ByteArray): Int {
    val buffer = this.buffer ?: throw Exception("stream closed")
    if (pos >= buffer.length()) return -1
    val readed = min(buffer.length() - pos, b.size)
    buffer.getBytes(pos, pos + readed, b)
    pos += readed
    return readed
  }
  
  override fun read(b: ByteArray, off: Int, len: Int): Int {
    val buffer = this.buffer ?: throw Exception("stream closed")
    if (pos >= buffer.length()) return -1
    val readed = min(buffer.length() - pos, len)
    buffer.getBytes(pos, pos + readed, b, off)
    pos += readed
    return readed
  }
  
  override fun close() {
    buffer = null
  }
}
