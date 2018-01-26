package com.satori.libs.testlib

import java.io.*
import java.nio.charset.*

inline fun OutputStream.nonClosableWrapper() = object : DelegatingOutputStream(this){
  override fun close() {
    // do not close
    // flush?
  }
}
inline fun<reified T> OutputStream.nonClosableWrapper(use: (OutputStream)->T):T = nonClosableWrapper().use(use)

//TODO: optimize, to avoid extra wrappers
inline fun OutputStream.nonClosableWriter(charset: Charset = Charsets.UTF_8) = nonClosableWrapper().writer(charset)
inline fun<reified T> OutputStream.nonClosableWriter(charset: Charset = Charsets.UTF_8, use: (OutputStreamWriter)->T):T = nonClosableWriter(charset).use(use)

inline fun<reified T> OutputStream.writer(use: (OutputStreamWriter)->T):T = writer().use(use)


