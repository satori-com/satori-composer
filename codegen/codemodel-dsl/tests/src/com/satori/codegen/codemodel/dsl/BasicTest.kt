package com.satori.codegen.codemodel.dsl

import com.satori.codegen.codemodel.dsl.builders.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.libs.common.kotlin.*
import com.sun.codemodel.*
import com.sun.codemodel.writer.*
import org.junit.*
import java.io.*

class BasicTest : Assert() {
  
  @Test
  fun `try it out`() {
    
    PACKAGE(System.out, BasicTest::class.java.`package`.name) {
      CLASS("Test") {
      }
    }
    
    println("done")
    System.out.flush()
  }
  
  companion object {
    fun PACKAGE(os: OutputStream, name: String, configure: IPackageScope.() -> Unit) {
      val model = JCodeModel()
      
      val pckg = PackageBuilder(model._package(name))
      pckg.configure()
      val cw = SingleStreamCodeWriter(os.nonClosableWrapper())
      try {
        model.build(cw)
      } finally {
        cw.close()
      }
    }
  }
}