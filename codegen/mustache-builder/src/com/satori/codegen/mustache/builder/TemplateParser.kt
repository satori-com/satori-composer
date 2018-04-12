package com.satori.codegen.mustache.builder

import com.github.mustachejava.*
import com.github.mustachejava.codes.*
import com.satori.codegen.mustache.builder.*

object TemplateParser {
  
  private fun visit(code: Code, visitor: ITemplateVisitor): ITemplateVisitor? {
    val name = code.name
    when (code) {
      is WriteCode -> return visitor.opWrite(code) //do nothing
      is NotIterableCode -> {
        if (name.endsWith("*")) {
          return visitor.opIfEmpty(name.removeSuffix("*"))
        } else if (name.endsWith("?")) {
          return visitor.opIfNot(name.removeSuffix("?"))
        } else {
          return visitor.opIfNull(name)
        }
      }
      is IterableCode -> {
        if (name.endsWith("*")) {
          return visitor.opFor(name.removeSuffix("*"))
        } else if (name.endsWith("?")) {
          return visitor.opIf(name.removeSuffix("?"))
        } else {
          return visitor.opIfNotNull(name)
        }
      }
      is ValueCode -> {
        if (name.endsWith("*") || name.endsWith("?")) {
          throw Exception("can't print value, not a string")
        }
        return visitor.opVarValue(name)
      }
      is PartialCode ->{
        return visitor.opPartial(name)
      }
      (null as DefaultMustacheVisitor?)._getEOF() -> return visitor // EOF, do nothing
      else -> {
        println("unknown directive '${name}': ${code.javaClass.typeName}")
        return visitor.opUnknown(code)
      }
    }
  }
  
  fun processCode(code: Code, visitor: ITemplateVisitor) {
    val cvisitor = visit(code, visitor)
    if (code is DefaultCode) {
      code._getAppended()?.let {
        visitor.append(it)
      }
    }
    if (cvisitor != null) {
      processCodes(code.codes, cvisitor)
    }
  }
  
  fun processCodes(codes: Array<Code>?, visitor: ITemplateVisitor) {
    codes?.forEach {
      processCode(it, visitor)
    }
  }
}
