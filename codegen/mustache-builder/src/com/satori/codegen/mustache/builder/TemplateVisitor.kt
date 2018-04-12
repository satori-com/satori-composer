package com.satori.codegen.mustache.builder

import com.github.mustachejava.*

class TemplateVisitor(val model: SchemaNode) : ITemplateVisitor {
  
  val ops = ArrayList<OpNode>()
  
  override fun append(text: String) {
    ops.add(OpNode.Write(text))
  }
  
  override fun opWrite(code: Code): ITemplateVisitor {
    return ErrorTemplateVisitor
  }
  
  override fun opVarValue(varName: String): ITemplateVisitor {
    ops.add(OpNode.WriteVarValue(varName))
    
    if (varName == "this") return ErrorTemplateVisitor
    
    model.declareChildObject(varName)
    return ErrorTemplateVisitor
  }
  
  override fun opIf(path: String): ITemplateVisitor {
    model.declareChildBoolean(path)
    return new(model).also { v ->
      ops.add(OpNode.If(path, v.ops))
    }
  }
  
  override fun opIfNot(varName: String): ITemplateVisitor {
    model.declareChildBoolean(varName)
    return new(model).also { v ->
      ops.add(OpNode.IfNot(varName, v.ops))
    }
  }
  
  override fun opIfEmpty(varName: String): ITemplateVisitor {
    model.declareChildArray(varName)
    return new(model).also { v ->
      ops.add(OpNode.IfEmpty(varName, v.ops))
    }
  }
  
  override fun opIfNull(varName: String): ITemplateVisitor {
    model.declareChildObject(varName)
    return new(model).also { v ->
      ops.add(OpNode.IfNull(varName, v.ops))
    }
  }
  
  override fun opIfNotNull(varName: String): ITemplateVisitor {
    return new(model.declareChildObject(varName)).also { v ->
      ops.add(OpNode.IfNotNull(varName, v.ops))
    }
  }
  
  override fun opFor(varName: String): ITemplateVisitor {
    return new(model.declareChildArray(varName)).also { v ->
      ops.add(OpNode.For(varName, v.ops))
    }
  }
  
  override fun opPartial(varName: String): ITemplateVisitor? {
    model.declareChildPartial(varName)
    ops.add(OpNode.Partial(varName))
    return ErrorTemplateVisitor
  }
  
  override fun opUnknown(code: Code): ITemplateVisitor {
    return ErrorTemplateVisitor
  }
  
  companion object {
    fun new(model: SchemaNode) = TemplateVisitor(model)
    fun new(model: String) = new(SchemaNode.newRootNode(model))
  }
}
