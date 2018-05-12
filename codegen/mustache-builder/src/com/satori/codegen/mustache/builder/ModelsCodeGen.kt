package com.satori.codegen.mustache.builder

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.codemodel.dsl.scopes.*
import java.io.*

object ModelsCodeGen {
  
  fun ITypeContainerScope.generateClass(name: String, schema: SchemaNode): ClassSpec {
    
    return CLASS(className(name)) {
      
      FIELD("_root", CREF(schema.root.className)) {
        FINAL()
      }
      
      if (schema.parent != null) {
        FIELD("_parent", CREF(schema.parent.className)) {
          FINAL()
        }
      }
      
      CTOR {
        if (schema.parent != null) {
          ARG("_root", CREF(schema.root.className))
          ARG("_parent", CREF(schema.parent.className))
          CODE("""
              this._root = _root;
              this._parent = _parent;
            """.trimIndent())
        } else {
          CODE("""
              _root = this;
            """.trimIndent())
        }
      }
      
      schema.children.forEach { _, cschema ->
        val className = cschema.className
        val varName = cschema.varName
        when (cschema.type) {
          SchemaNode.Type.ARRAY -> {
            val listType = if (cschema.hasChildren()) {
              //val itemClassName = "${className(cname)}Item"
              generateClass(className, cschema)
              
              val listType = CREF<ArrayList<*>>().narrow(CREF(className))
              FIELD(cschema.varName, listType) {
                FINAL()
                INIT(NEW(listType))
              }
              
              FUN("add$className") {
                RETURNS(CREF(className))
                CODE("""
                    $className _item = new $className(_root, this);
                    $varName.add(_item);
                    return _item;
                  """.trimIndent())
              }
            } else {
              val listType = CREF<ArrayList<*>>().narrow(CREF<String>())
              FIELD(varName, listType) {
                FINAL()
                INIT(NEW(listType))
              }
              
              FUN("add$className") {
                ARG<String>("_item")
                CODE("""
                    $varName.add(_item);
                  """.trimIndent())
              }
            }
            
          }
          SchemaNode.Type.OBJECT -> {
            if (cschema.hasChildren()) {
              generateClass(className, cschema)
              FIELD(varName, className)
              
              FUN("set$className") {
                RETURNS(CREF(className))
                CODE("""
                    $varName = new $className(_root, this);
                    return $varName;
                  """.trimIndent())
              }
              
            } else {
              FIELD<String>(varName)
            }
          }
          SchemaNode.Type.BOOLEAN -> {
            FIELD(varName, Boolean::class.java)
          }
          SchemaNode.Type.PARTIAL -> {
            FIELD(varName, CREF("${className}Model"))
          }
        }
      }
    }
  }
  
  fun generate(pckg: String, schema: SchemaNode, os: OutputStream) {
    FILE(os) {
      COMMENT("""
          auto generated
          don't modify
        """.trimIndent())
      PACKAGE(pckg) {
        generateClass(schema.name, schema)
      }
    }
  }
}