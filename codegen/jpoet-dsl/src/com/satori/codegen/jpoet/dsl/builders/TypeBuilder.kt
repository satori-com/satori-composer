package com.satori.codegen.jpoet.dsl.builders

import com.satori.codegen.jpoet.dsl.*
import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*
import javax.lang.model.element.*

open class TypeBuilder(builder: TypeSpec.Builder) : ITypeScope {
  var access = AccessModifier.PUBLIC
  val builder = builder
  
  constructor(name: String) : this(TypeSpec.classBuilder(name))
  
  fun build(): TypeSpec {
    when (access) {
      AccessModifier.PUBLIC -> builder.addModifiers(Modifier.PUBLIC)
      AccessModifier.PRIVATE -> builder.addModifiers(Modifier.PRIVATE)
      AccessModifier.PROTECTED -> builder.addModifiers(Modifier.PROTECTED)
    }
    return builder.build()
  }
  
  // ITypeScope implementation
  
  override fun EXTENDS(type: TypeName) {
    builder.superclass(type)
  }
  
  override fun CTOR(configure: IMethodScope.() -> Unit) {
    val ctorBuilder = MethodBuilder().apply {
      access = null
      configure()
    }
    builder.addMethod(ctorBuilder.build())
  }
  
  override fun FUN(name: String, configure: IMethodScope.() -> Unit) {
    val methodBuilder = MethodBuilder(methodName(name))
    methodBuilder.configure()
    builder.addMethod(methodBuilder.build())
  }
  
  override fun FIELD(name: String, type: TypeName, configure: IFieldScope.() -> Unit) {
    val fieldBuilder = FieldBuilder(FieldSpec.builder(type, fieldName(name)))
    fieldBuilder.configure()
    builder.addField(fieldBuilder.build())
  }
  
  override fun GETTER(name: String, configure: IMethodScope.() -> Unit) {
    val methodBuilder = MethodBuilder(getterName(name))
    methodBuilder.configure()
    builder.addMethod(methodBuilder.build())
  }
  
  override fun IMPLEMENTS(type: TypeName) {
    builder.addSuperinterface(type)
  }
  
  override fun STATIC() {
    builder.addModifiers(Modifier.STATIC)
  }
  
  // IHasAccessModifier implementation
  
  override fun ACCESS(value: AccessModifier) {
    access = value
  }
  
  // ITypeContainerScope
  
  override fun TYPE(type: TypeSpec) {
    builder.addType(type)
  }
  
  // IAnnotateableScope
  
  override fun ANNOTATION(type: ClassName, configure: IAnnotationScope.() -> Unit) {
    builder.addAnnotation(AnnotationBuilder(type).apply {
      configure()
    }.build())
  }
}
