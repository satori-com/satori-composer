package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.*
import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*
import javax.lang.model.element.*

class MethodBuilder(val builder: MethodSpec.Builder) : IMethodScope {
  var access: AccessModifier? = AccessModifier.PUBLIC
  
  constructor(name: String) : this(MethodSpec.methodBuilder(name))
  constructor() : this(MethodSpec.constructorBuilder())
  
  fun build(): MethodSpec {
    when (access) {
      AccessModifier.PUBLIC -> builder.addModifiers(Modifier.PUBLIC)
      AccessModifier.PRIVATE -> builder.addModifiers(Modifier.PRIVATE)
      AccessModifier.PROTECTED -> builder.addModifiers(Modifier.PROTECTED)
    }
    return builder.build()
  }
  
  // IMethodScope implementation
  
  override fun ARG(name: String, type: TypeName) {
    builder.addParameter(type, name)
  }
  
  override fun RETURNS(type: TypeName) {
    builder.returns(type)
  }
  
  override fun CODE(statements: String) {
    builder.addCode(statements)
    builder.addCode("\n")
  }
  
  override fun STMT(statement: String) {
    builder.addStatement(statement)
  }
  
  override fun STMT(fmt: String, vararg args: Any) {
    builder.addStatement(fmt, *args)
  }
  
  override fun ABSTRACT() {
    builder.addModifiers(Modifier.ABSTRACT)
  }
  
  // IHasAccessModifier implementation
  override fun ACCESS(value: AccessModifier) {
    access = value
  }
  
  // IAnnotateableScope implementation
  override fun ANNOTATION(type: ClassName, configure: IAnnotationScope.() -> Unit) {
    builder.addAnnotation(AnnotationBuilder(type).apply {
      configure()
    }.build())
  }
}


