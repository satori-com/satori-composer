package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

class PackageBuilder(private val typeWriter: (TypeSpec) -> Unit) : IPackageScope {
  
  // IPackageScope implementation
  
  override fun IMPORT() {
  }
  
  // ITypeContainerScope implementation
  
  override fun TYPE(type: TypeSpec) {
    typeWriter(type)
  }
}
