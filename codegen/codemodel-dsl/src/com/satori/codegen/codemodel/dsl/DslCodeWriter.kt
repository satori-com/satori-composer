package com.satori.codegen.codemodel.dsl

import com.sun.codemodel.*
import java.io.*

class DslCodeWriter(os: OutputStream) : CodeWriter(), Closeable {
  val os = PrintStream(os)
  
  override fun openBinary(pkg: JPackage?, fileName: String?) = os
  override fun close() {
    os.close()
  }
}
