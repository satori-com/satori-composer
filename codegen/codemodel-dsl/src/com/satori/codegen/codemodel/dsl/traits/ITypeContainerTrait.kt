package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.builders.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

interface ITypeContainerTrait : ITypeContainerScope {
  val typeContainerTrait: JClassContainer
  
  override fun CLASS(name: String, configure: ITypeScope.() -> Unit): ClassSpec {
    val jclass = typeContainerTrait._class(name)
    TypeBuilder(jclass).configure()
    return ClassSpec(jclass)
  }
  
  override fun ENUM(name: String, configure: IEnumScope.() -> Unit) {
    EnumBuilder(typeContainerTrait._enum(name)).configure()
  }
  
  override fun INTERFACE(name: String, configure: ITypeScope.() -> Unit) {
    TypeBuilder(typeContainerTrait._interface(name)).configure()
  }
}
