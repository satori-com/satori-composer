package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.libs.common.kotlin.*
import com.sun.codemodel.*
import java.io.*

class FileBuilder : BaseBuilder(JCodeModel()), IFileScope {
  val comments = ArrayList<String>()
  
  // IFileScope implementation
  
  override fun COMMENT(text: String) {
    comments.addAll(text.lines())
  }
  
  fun build(os: OutputStream) {
    os.nonClosableWrapper { out ->
      out.writer { ow ->
        for (c in comments) {
          ow.write("//"); ow.write(c); ow.write("\n")
        }
        if (!comments.isEmpty()) {
          ow.write("\n")
        }
      }
      DslCodeWriter(os.nonClosableWrapper()).use { cw ->
        jmodel.build(cw)
      }
    }
  }
  
  override fun PACKAGE(name: String, configure: IPackageScope.() -> Unit) {
    PackageBuilder(jmodel._package(name)).apply {
      configure()
    }
  }
}
