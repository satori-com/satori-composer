@file: JvmName("App")

package com.satori.codegen.jpoet.dsl.example

import java.nio.file.*

fun main(args: Array<String>) {
  println("model-code-gen version: 0.0")
  println("java version: ${System.getProperty("java.version")}")
  
  val config = ModelsCodeGenConfig().apply {
    println("command line arguments:")
    val itor = args.iterator()
    while (true) {
      val name = if (itor.hasNext()) itor.next() else break
      val value = if (itor.hasNext()) itor.next() else throw Exception("argument value is missing")
      if (!name.startsWith("-")) throw Exception("argument '$name' should start with '-'")
      println("${name} ${value}")
      when (name) {
        "-pckg" -> pckg = value.trim()
        "-out" -> outDir = Paths.get(value)
        "-schema" -> schemaPath = Paths.get(value)
        "-prefix" -> classPrefix = value.trim()
        else -> throw Exception("unknown argument '$name'")
      }
      
    }
  }
  ModelsCodeGen(config).generateCode()
}


