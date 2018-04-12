package com.satori.codegen.codemodel.dsl

import com.satori.codegen.codemodel.dsl.builders.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.libs.common.kotlin.*
import com.sun.codemodel.*
import java.io.*

fun PACKAGE(os: OutputStream, name: String, configure: IPackageScope.() -> Unit) {
  val model = JCodeModel()
  
  val pckg = PackageBuilder(model._package(name))
  pckg.configure()
  DslCodeWriter(os.nonClosableWrapper()).use { cw ->
    model.build(cw)
  }
}

fun FILE(os: OutputStream, configure: IFileScope.() -> Unit) {
  FileBuilder().apply {
    configure()
    build(os)
  }
}

fun JClass.inner(name: String): JClass = NestedJClass(name, this)