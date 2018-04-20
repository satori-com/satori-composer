@file:JvmName("Example")

package com.satori.codegen.jschema.models.gen

import java.io.*

fun main(args: Array<String>) {
  
  File("core/.tests-data").deleteRecursively()
  
  val cfg = AppConfig().apply {
    pckg = javaClass.`package`.name
    schema = "core/codegen/schema.json"
    out = "core/.tests-data"
  }
  
  val env = ICodeGenEnv.Default(cfg)
  ModelsCodeGen.run(env)
}
