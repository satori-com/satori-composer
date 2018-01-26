package com.satori.libs.testlib

import java.io.*

open class DelegatingOutputStream(val os: OutputStream) : OutputStream() {
  override fun write(b: ByteArray) {
    os.write(b)
  }
  
  override fun write(b: ByteArray, off: Int, len: Int) {
    os.write(b, off, len)
  }
  
  override fun write(b: Int) {
    os.write(b)
  }
  
  override fun close() {
    os.close()
  }
  
  override fun flush() {
    os.flush()
  }
}
