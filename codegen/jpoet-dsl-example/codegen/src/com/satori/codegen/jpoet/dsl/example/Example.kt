@file:JvmName("Example")

package com.satori.codegen.jpoet.dsl.example

import java.nio.file.*


fun main(args: Array<String>) {
  
  Paths.get("scene/graphql/.tests-data").toFile().deleteRecursively()
  
  val config = ModelsCodeGenConfig().apply {
    //pckg = value.trim()
    outDir = Paths.get("scene/graphql/.tests-data")
    schemaPath = Paths.get("scene/graphql/graphql/example.graphqls")
    //classPrefix = value.trim()
  }
  ModelsCodeGen(config).generateCode()
}
