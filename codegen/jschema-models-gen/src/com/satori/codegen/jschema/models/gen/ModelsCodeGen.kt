package com.satori.codegen.jschema.models.gen

import com.fasterxml.jackson.module.jsonSchema.*
import com.fasterxml.jackson.module.jsonSchema.types.*

class ModelsCodeGen(env: ICodeGenEnv) : ICodeGenEnv by env {
  
  fun run() {
    val model = FileModel()
    
    if (schema !is ObjectSchema) {
      println("schema is no an object")
      return
    }
    
    val className = fmt.className(cfg.name ?: throw Exception("--name not specified"))
    model.packageName = cfg.pckg
    
    model.type = TypeModel().also {
      it.className = className
      processProperties(it, schema)
    }
    
    
    
    writeJavaClass(className) {
      FileTemplate.render(it, model)
    }
  }
  
  fun processProperties(model: TypeModel, schema: ObjectSchema) {
    for ((k, v) in schema.properties) {
      model.addPropertiesItem().apply {
        this.schemaName = k
        this.varName = fmt.varName(k)
        this.type = typeForSchema(k, v)
      }
      if (v.isObjectSchema) {
        model.addTypesItem().apply {
          type = TypeModel().also {
            it.isStatic = true
            it.className = fmt.className(k)
            processProperties(it, v.asObjectSchema())
          }
        }
      }
    }
  }
  
  fun typeForSchema(propName: String, schema: JsonSchema): String {
    if (schema.isStringSchema) return "String"
    if (schema.isBooleanSchema) return "Boolean"
    if (schema.isIntegerSchema) return "Integer"
    if (schema.isNumberSchema) return "Double"
    if (schema.isAnySchema) return "JsonNode"
    if (schema.isObjectSchema) return fmt.className(propName)
    throw Exception("type '$schema' not supported")
  }
  
  companion object {
    
    fun run(env: ICodeGenEnv) {
      ModelsCodeGen(env).run()
    }
    
  }
}
