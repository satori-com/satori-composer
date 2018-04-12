package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.builders.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

interface ITypeTrait : ITypeScope, IHasAnnotationsTrait, IHasCommentsTrait, ITypeContainerTrait, IHasAccessModifierTrait {
  val typeTrait: JDefinedClass
  
  override val hasCommentsTrait: JDocComment
    get() = typeTrait.javadoc()
  
  override val typeContainerTrait: JClassContainer
    get() = typeTrait
  
  override fun EXTENDS(type: JClass) {
    typeTrait._extends(type)
  }
  
  override fun CTOR(configure: IMethodScope.() -> Unit) {
    CtorBuilder(typeTrait).apply {
      PUBLIC()
      configure()
      build()
    }
  }
  
  override fun FUN(name: String, configure: IMethodScope.() -> Unit) {
    MethodBuilder(name, typeTrait).apply {
      PUBLIC()
      configure()
      build()
    }
  }
  
  override fun FIELD(name: String, type: JType, configure: IFieldScope.() -> Unit) {
    FieldBuilder(name, type).apply {
      PUBLIC()
      configure()
    }.build(typeTrait)
  }
  
  override fun GETTER(name: String, configure: IMethodScope.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun IMPLEMENTS(type: JClass) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun STATIC() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}


