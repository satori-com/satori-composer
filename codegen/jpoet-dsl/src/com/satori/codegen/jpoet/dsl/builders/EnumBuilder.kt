package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*

open class EnumBuilder(builder: TypeSpec.Builder) : TypeBuilder(builder), IEnumScope {
  
  constructor(name: String) : this(TypeSpec.enumBuilder(name))
  
  // IEnumScope implementation
  
  override fun OPTION(name: String, configure: IAnonymousClassScope.() -> Unit) {
    val scope = AnonymousClassBuilder()
    scope.configure()
    builder.addEnumConstant(name, scope.build())
  }
  
}
