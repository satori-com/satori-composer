package com.satori.codegen.mustache.builder

import com.satori.codegen.utils.*

open class BaseSchemaNode(val name: String, val type: SchemaNode.Type, val parent: SchemaNode?) {
  val varName = CodeFormatter.varName(name)
  val className: String
  val children = HashMap<String, SchemaNode>()
  
  init {
    className = CodeFormatter.className(name).let {
      if (type == SchemaNode.Type.ARRAY) "${it}Item" else it
    }
  }
}
