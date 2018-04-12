package com.satori.codegen.mustache.builder

abstract class InstructionNode(val code: Code) {
  enum class Code {
    WRITE, WRITE_VAR_VALUE, IF, IF_NOT, IF_EMPTY, IF_NULL, IF_NOT_NULL, FOR
  }
  
  class Write(val text: String) : InstructionNode(Code.WRITE) {
  }
  
  class WriteVarValue(val varName: String) : InstructionNode(Code.WRITE_VAR_VALUE) {
  }
  
  class If(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.IF) {
  }
  
  class IfNot(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.IF_NOT) {
  }
  
  class IfEmpty(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.IF_EMPTY) {
  }
  
  class IfNull(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.IF_NULL) {
  }
  
  class IfNotNull(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.IF_NOT_NULL) {
  }
  
  class For(val varName: String, val instructions: ArrayList<InstructionNode>) : InstructionNode(Code.FOR) {
  }
  
}

class BlockNode(ctx: SchemaNode) {

}

class FunNode(ctx: SchemaNode, parent: FunNode?) {
  val ctx = ctx
  val parent = parent
  val instructions = ArrayList<InstructionNode>()
  
  fun opWrite(text: String): FunNode {
    instructions.add(InstructionNode.Write(text))
    return this
  }
  
  fun opWriteVarValue(varName: String): FunNode {
    instructions.add(InstructionNode.WriteVarValue(varName))
    return this
  }
  
  fun opIf(name: String): FunNode {
    val c = ctx.declareChildBoolean(name)
    val n = FunNode(ctx, this)
    instructions.add(InstructionNode.If(c.varName, n.instructions))
    return n
  }
  
  //...
  
  fun IfNotNull(name: String): FunNode {
    val c = ctx.declareChildObject(name)
    val n = FunNode(c, this)
    instructions.add(InstructionNode.If(c.varName, n.instructions))
    return n
  }
  
  companion object {
    fun newRootNode(name: String, ctx: SchemaNode) = FunNode(ctx, null)
  }
}
