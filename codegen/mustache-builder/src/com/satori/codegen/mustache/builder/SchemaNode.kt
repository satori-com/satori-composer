package com.satori.codegen.mustache.builder

import com.satori.codegen.utils.*
import java.io.*
import java.util.*

class SchemaNode : BaseSchemaNode {
  
  val root: SchemaNode
  
  enum class Type {
    ARRAY, OBJECT, BOOLEAN, PARTIAL
  }
  
  private constructor(name: String, type: Type, parent: SchemaNode?, root: SchemaNode) : super(name, type, parent) {
    this.root = root
  }
  
  private constructor(name: String, type: Type, parent: SchemaNode?) : super(name, type, parent) {
    this.root = this
  }
  
  fun at(path: String): SchemaNode? {
    var node = if (path.startsWith(":")) root else this
    val itor = StringTokenizer(path, ":")
    if (!itor.hasMoreTokens()) throw Exception("invalid path")
    while (true) {
      val cname = itor.nextToken()
      if (cname == "..") {
        node = node.parent!!
      } else {
        node = node.children[cname] ?: return null
      }
      if (!itor.hasMoreTokens()) break
    }
    return node
  }
  
  fun declarePath(path: String, type: Type): SchemaNode {
    var node = if (path.startsWith(":")) root else this
    val itor = StringTokenizer(path, ":")
    if (!itor.hasMoreTokens()) throw Exception("invalid path")
    while (true) {
      val cname = itor.nextToken()
      if (!itor.hasMoreTokens()) {
        if (cname == "..") {
          if (node.parent!!.type == type) throw Exception("type collision")
          return node.parent!!
        }
        return node.declareChild(cname, type)
      }
      if (cname == "..") {
        node = node.parent!!
        continue
      }
      node = node.declareChild(cname, Type.OBJECT)
    }
  }
  
  fun declareChild(name: String, type: Type): SchemaNode {
    if (name.isEmpty() || !name.all { it.isLetterOrDigit() }) {
      throw Exception("invalid name: '$name'")
    }
    if (this.type == Type.BOOLEAN || this.type == Type.PARTIAL) {
      throw Exception("'$type' node can't have children")
    }
    
    val child = children.get(name)
    if (child == null) {
      val schema = SchemaNode(name, type, this, root)
      children.put(name, schema)
      return schema
    }
    
    if (child.type != type) throw Exception("type collision '${path()}/$name'")
    return child
  }
  
  fun declareChildArray(name: String) = declarePath(name, Type.ARRAY)
  fun declareChildObject(name: String) = declarePath(name, Type.OBJECT)
  fun declareChildBoolean(name: String) = declarePath(name, Type.BOOLEAN)
  fun declareChildPartial(name: String) = declarePath(name, Type.PARTIAL)
  
  fun isObject() = type == Type.OBJECT
  fun isArray() = type == Type.ARRAY
  fun isBoolean() = type == Type.BOOLEAN
  fun isPartial() = type == Type.PARTIAL
  
  fun hasChildren() = !children.isEmpty()
  
  fun path(): String {
    if (parent === null) {
      return name
    }
    val pname = when (type) {
      Type.ARRAY -> "$name*"
      Type.OBJECT -> "$name"
      Type.BOOLEAN -> "$name?"
      Type.PARTIAL -> ">$name"
    }
    return "${parent.path()}:$pname"
  }
  
  fun getTypeName(): String {
    val baseType =
      if (children.isEmpty()) {
        "String"
      } else if (type == Type.ARRAY) {
        "${CodeFormatter.className(name)}Item"
      } else {
        CodeFormatter.className(name)
      }
    if (type == Type.ARRAY) {
      return "$baseType[]"
    }
    return baseType
  }
  
  fun printTree(sw: OutputStreamWriter, indent: String) {
    sw.write(indent)
    if (parent == null) {
      sw.write("- /\n")
    } else {
      sw.write("- $name : ${getTypeName()}\n")
    }
    val i = indent + "  "
    for (c in children.values) {
      c.printTree(sw, i)
    }
    
  }
  
  companion object {
    fun newRootNode(name: String) = SchemaNode(name, Type.OBJECT, null)
  }
}
