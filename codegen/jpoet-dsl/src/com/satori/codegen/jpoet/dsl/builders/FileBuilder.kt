package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.scopes.*
import com.satori.libs.common.kotlin.*
import com.squareup.javapoet.*
import java.io.*

class FileBuilder(val ostream: OutputStream) : IFileScope {
  val comments = ArrayList<String>()
  
  // IFileScope implementation
  
  override fun COMMENT(line: String) {
    comments.add(line)
  }
  
  override fun PACKAGE(name: String, configure: IPackageScope.() -> Unit) {
    PackageBuilder { typeSpec ->
      val javaFile = JavaFile.builder(name, typeSpec).apply {
        skipJavaLangImports(true)
        addFileComment(comments.joinToString("\n"))
      }.build()
      
      ostream.nonClosableWrapper().bufferedWriter().use {
        javaFile.writeTo(it)
        it.flush()
      }
    }.configure()
  }
}
