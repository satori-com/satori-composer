package com.satori.codegen.jschema.models.gen

import com.fasterxml.jackson.module.jsonSchema.*
import com.satori.libs.common.kotlin.*
import com.satori.libs.common.kotlin.json.*
import java.io.*
import java.nio.file.*

fun ICodeGenEnv.loadSchema(path: String): JsonSchema {
  println("reading schema '$path'...")
  Files.newInputStream(Paths.get(path)).use {
    return jsonParse(it)
  }
}

fun List<String>.concat(): String {
  val sb = StringBuilder()
  forEach { sb.append(it) }
  return sb.toString()
}

fun ICodeGenEnv.writeJavaClass(className: String, block: (Writer) -> Unit) {
  file(path(cfg.pckg, "${className}.java")) {
    write { os ->
      os.writer().use { ow ->
        block(ow)
      }
    }
    /*val code = StringWriter().use {
      block(it)
      it.toString()
    }
    println(code)
    val cu = JavaParser.parse(code)*/
    //write(cu.toString())
    println("file generated '${path.fileName}' ('${path}')")
    println("verifying '${path.fileName}' ('${path}')...")
    //JavaParser.parse(path)
  }
}
