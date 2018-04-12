package com.satori.codegen.mustache.builder

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import com.satori.codegen.utils.*
import com.sun.codemodel.*
import java.io.*
import java.util.*

class RendererCodeGen(val scope: ITypeScope) {
  
  private var id = 0
  
  class FunScope(
    val model: SchemaNode,
    scope: IMethodScope
  ) : IMethodScope by scope {
    var indent = ""
    var indentLevel = 0
      set(value) {
        field = value
        indent = " ".repeat(value)
      }
    
    inline fun indent(block: () -> Unit) {
      indentLevel += 2
      try {
        block()
      } finally {
        indentLevel -= 2
      }
    }
    
    fun String.indent() = trimIndent().prependIndent(indent)
  }
  
  fun RENDER_FUN(name: String, model: SchemaNode, block: FunScope.() -> Unit) {
    scope.FUN(name) {
      STATIC()
      THROWS<Exception>()
      ARG<Writer>("_os")
      ARG("_model", resolveType(model))
      FunScope(model, this).block()
    }
  }
  
  fun RENDER_FOR_FUN(name: String, model: SchemaNode, block: FunScope.() -> Unit) {
    scope.FUN(name) {
      STATIC()
      THROWS<Exception>()
      ARG<Writer>("_os")
      ARG("_model", resolveType(model))
      ARG("_last", TREF(Boolean::class.java))
      FunScope(model, this).block()
    }
  }
  
  fun generateFun(ops: ArrayList<OpNode>, model: SchemaNode): String {
    val funName = "render${id++}"
    generateFun(funName, ops, model, AccessModifier.PRIVATE)
    return funName
  }
  
  fun generateFun(name: String, ops: ArrayList<OpNode>, model: SchemaNode) {
    generateFun(name, ops, model, AccessModifier.PUBLIC)
  }
  
  fun generateFun(name: String, ops: ArrayList<OpNode>, model: SchemaNode, access: AccessModifier) {
    RENDER_FUN(name, model) {
      ACCESS(access)
      ops.forEach {
        processCode(it, model)
      }
    }
  }
  
  fun generateForFun(ops: ArrayList<OpNode>, model: SchemaNode): String {
    val funName = "render${id++}"
    RENDER_FOR_FUN(funName, model) {
      ops.forEach {
        processCode(it, model)
      }
    }
    return funName
  }
  
  private fun FunScope.processForEach(op: OpNode.For, model: SchemaNode) {
    val funName = generateForFun(op.ops, model.at(op.varName)!!)
    val varName = translatePath(op.varName)
    CODE("""
      for(int _i=0; _i< $varName.size(); _i+=1){
        $funName(_os, $varName.get(_i), (_i+1) >= $varName.size());
      }
    """.indent())
    
  }
  
  private fun FunScope.processIfNotNull(op: OpNode.IfNotNull, model: SchemaNode) {
    val varName = translatePath(op.varName)
    val funName = generateFun(op.ops, model.at(op.varName)!!)
    CODE("""
      if($varName != null){
        $funName(_os, $varName);
      }
    """.indent())
    
  }
  
  private fun escapeSpecialChars(text: String): String {
    val sb = StringBuilder(text.length * 2)
    for (c in text) {
      if (c == '\r') {
        continue
      }
      if (c == '\t') {
        sb.append("\\t")
      } else if (c == '\n') {
        sb.append("\\n")
      } else if (c == '"') {
        sb.append("\\\"")
      } else {
        sb.append(c)
      }
    }
    return sb.toString()
  }
  
  private fun IScope.translatePath(path: String): String {
    when (path) {
      "this" -> return "_model"
      "last" -> return "_last"
    }
    val sb = StringBuilder()
    sb.append("_model")
    if (path.startsWith(":")) {
      sb.append("._root")
    }
    val itor = StringTokenizer(path, ":")
    while (itor.hasMoreTokens()) {
      val name = itor.nextToken()
      when (name) {
        ".." -> {
          sb.append("._parent")
        }
        else -> {
          sb.append(".${varName(name)}")
        }
      }
    }
    return sb.toString()
  }
  
  private fun FunScope.processCode(op: OpNode, model: SchemaNode) {
    when (op) {
      is OpNode.Write -> {
        CODE("""
          _os.write("${escapeSpecialChars(op.text)}");
        """.indent())
      }
      is OpNode.For -> {
        processForEach(op, model)
      }
      is OpNode.If -> {
        val varName = translatePath(op.varName)
        CODE("if( ${varName} ) {\n".indent())
        indent {
          op.ops.forEach { processCode(it, model) }
        }
        CODE("}\n".indent())
      }
      is OpNode.IfNull -> {
        val varName = translatePath(op.varName)
        CODE("if( ${varName} == null ) {\n".indent())
        indent {
          op.ops.forEach { processCode(it, model) }
        }
        CODE("}\n".indent())
      }
      is OpNode.IfNot -> {
        val varName = translatePath(op.varName)
        CODE("if( !$varName ) {\n".indent())
        indent {
          op.ops.forEach { processCode(it, model) }
        }
        CODE("}\n".indent())
      }
      is OpNode.IfNotNull -> {
        processIfNotNull(op, model)
      }
      is OpNode.WriteVarValue -> {
        val varName = if (op.varName == "this") {
          "_model"
        } else {
          "_model.${op.varName}"
        }
        CODE("""
          _os.write($varName);
        """.indent())
      }
      is OpNode.Partial -> {
        val templateClass = "${CodeFormatter.className(op.name)}Template"
        val varName = CodeFormatter.varName(op.name)
        CODE("""
          $templateClass.render(_os, _model.$varName);
        """.indent())
      }
      else -> throw Exception("unsupported operation '${op.code}'")
    }
  }
  
  fun IScope.modelClassName(model: SchemaNode): JClass {
    val className = if (model.isArray()) {
      "${className(model.name)}Item"
    } else {
      className(model.name)
    }
    if (model.parent == null) {
      return CREF(className)
    }
    return modelClassName(model.parent).inner(className)
  }
  
  fun IScope.resolveType(model: SchemaNode): JType {
    return if (model.isArray()) {
      val base = if (!model.hasChildren()) {
        CREF<String>()
      } else {
        modelClassName(model)
      }
      base
    } else if (model.isBoolean()) {
      TREF(Boolean::class.java)
    } else if (!model.hasChildren()) {
      CREF<String>()
    } else {
      modelClassName(model)
    }
  }
  
  companion object {
    fun generate(pckg: String, className: String, template: TemplateVisitor, os: OutputStream) {
      PACKAGE(os, pckg) {
        CLASS(className) {
          COMMENT("""
            auto generated
            don't modify
          """.trimIndent())
          RendererCodeGen(this).generateFun("render", template.ops, template.model)
        }
      }
    }
    
    fun generate(pckg: String, className: String, ops: ArrayList<OpNode>, model: SchemaNode, os: OutputStream) {
      PACKAGE(os, pckg) {
        CLASS(className) {
          COMMENT("""
            auto generated
            don't modify
          """.trimIndent())
          RendererCodeGen(this).generateFun("render", ops, model)
        }
      }
    }
    
  }
  
}