package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

class PackageBuilder(val jdef: JPackage) : BaseBuilder(jdef.owner()), IPackageScope {
  
  override fun IMPORT() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun CLASS(name: String, configure: ITypeScope.() -> Unit): ClassSpec {
    val jclass = jdef._class(name)
    TypeBuilder(jclass).configure()
    return ClassSpec(jclass)
  }
  
  override fun ENUM(name: String, configure: IEnumScope.() -> Unit) {
    EnumBuilder(jdef._enum(name)).configure()
  }
  
  override fun INTERFACE(name: String, configure: ITypeScope.() -> Unit) {
    TypeBuilder(jdef._interface(name)).configure()
  }
}
