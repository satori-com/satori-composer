package com.satori.codegen.jschema.models.gen

import com.fasterxml.jackson.module.jsonSchema.*
import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.json.*
import java.nio.file.*

interface ICodeGenEnv : IJsonContext {
  val cfg: AppConfig
  val schema: JsonSchema
  val fmt: ICodeFormatter
    get() = CodeFormatter
  
  fun path(pckg: String, fileName: String): Path {
    val pckgDir = pckg.replace(".", "/")
    return Paths.get("${cfg.out}/$pckgDir/$fileName")
  }
  
  class Default(cfg: AppConfig) : ICodeGenEnv {
    override val mapper = DefaultJsonContext.mapper
    override val cfg = cfg
    override val schema = loadSchema(cfg.schema
      ?: throw Exception("missing '--schema' argument"))
  }
  
}
