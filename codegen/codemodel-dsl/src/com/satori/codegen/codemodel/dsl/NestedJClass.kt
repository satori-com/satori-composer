package com.satori.codegen.codemodel.dsl

import com.sun.codemodel.*

class NestedJClass(val _name: String, val _outer: JClass) : JClass(_outer.owner()) {
  val _package = _outer._package()
  override fun substituteParams(variables: Array<out JTypeVar>?, bindings: MutableList<JClass>?): JClass {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun isInterface(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun fullName(): String {
    return "${_outer.fullName()}.$_name"
  }
  
  override fun _implements(): MutableIterator<JClass> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun isAbstract(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun name(): String {
    return _name
  }
  
  override fun _extends(): JClass {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun _package(): JPackage {
    return _package
  }
  
  override fun outer(): JClass {
    return _outer
  }
}