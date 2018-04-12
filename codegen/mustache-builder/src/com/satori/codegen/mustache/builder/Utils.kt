package com.satori.codegen.mustache.builder

import com.satori.codegen.codemodel.dsl.*
import com.satori.libs.common.kotlin.*
import java.nio.file.*

fun List<String>.concat(): String {
  val sb = StringBuilder()
  forEach { sb.append(it) }
  return sb.toString()
}

fun ICodeGenEnv.parseTemplate(templatePath: Path): TemplateVisitor {
  println("parsing template '$templatePath'...")
  Files.newBufferedReader(templatePath).use {
    return TemplateParser.parse(it, "${templatePath.fileNameWithoutExt()}Model")
  }
}

fun ICodeGenEnv.path(className: ClassName): Path {
  val pckgDir = className.pckg.replace(".", "/")
  return Paths.get("${cfg.out}/$pckgDir/${className.name}.java")
}
