package com.satori.codegen.mustache.builder

import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.*
import java.io.*
import java.nio.file.*

interface ICodeGenEnv {
  val cfg: AppConfig
  //val modelName: String
  val templates: ArrayList<Path>
  val fmt: ICodeFormatter get() = CodeFormatter
  
  class Default(cfg: AppConfig) : ICodeGenEnv {
    override val cfg = cfg
    override val templates: ArrayList<Path>
    
    init {
      templates = cfg.templates?.split(File.pathSeparator)?.select { Paths.get(it) } ?: throw Exception("missing '--templates argument'")
    }
  }
  
  companion object {
    fun default(cfg: AppConfig, block: ICodeGenEnv.() -> Unit) {
      block(Default(cfg))
    }
  }
}
