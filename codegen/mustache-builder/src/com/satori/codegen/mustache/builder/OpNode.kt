package com.satori.codegen.mustache.builder

abstract class OpNode(val code: Code) {
  enum class Code {
    WRITE, WRITE_VAR_VALUE, IF, IF_NOT, IF_EMPTY, IF_NULL, IF_NOT_NULL, FOR, PARTIAL
  }
  
  class Write(val text: String) : OpNode(Code.WRITE) {
  }
  
  class WriteVarValue(val varName: String) : OpNode(Code.WRITE_VAR_VALUE) {
  }
  
  class If(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.IF) {
  }
  
  class IfNot(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.IF_NOT) {
  }
  
  class IfEmpty(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.IF_EMPTY) {
  }
  
  class IfNull(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.IF_NULL) {
  }
  
  class IfNotNull(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.IF_NOT_NULL) {
  }
  
  class For(val varName: String, val ops: ArrayList<OpNode>) : OpNode(Code.FOR) {
  }
  
  class Partial(val name: String) : OpNode(Code.PARTIAL) {
  }
}
