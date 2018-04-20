package com.satori.codegen.jschema.models.gen

import com.satori.libs.common.kotlin.json.*

object App : IJsonContext by DefaultJsonContext {
  @JvmStatic
  fun main(vararg args: String) {
    println(MetaInfo)
    println("java version: ${System.getProperty("java.version")}")
    
    val cfg = jsonObject {
      val itor = args.iterator()
      while (true) {
        val name = if (itor.hasNext()) itor.next() else break
        val value = if (itor.hasNext()) itor.next() else throw Exception("argument value is missing")
        if (!name.startsWith("--")) throw Exception("argument '$name' should start with '--'")
        field(name, value)
      }
    }.scope { toValue<AppConfig>() }
    
    val env = ICodeGenEnv.Default(cfg)
    ModelsCodeGen.run(env)
  }
}


