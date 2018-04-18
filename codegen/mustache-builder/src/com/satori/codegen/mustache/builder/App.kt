package com.satori.codegen.mustache.builder

import com.satori.codegen.codemodel.dsl.*
import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.*
import com.satori.libs.common.kotlin.json.*
import java.nio.file.*

object App : IJsonContext by DefaultJsonContext {
  
  @JvmStatic
  fun main(vararg args: String) {
    println("mustache-builder version: 0.0")
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
    
    ICodeGenEnv.default(cfg) {
      templates.forEach { templatePath ->
        
        // parse template
        println("parsing template '$templatePath'...")
        val baseClassName = fmt.className(templatePath.fileNameWithoutExt())
        val modelClassName = "${baseClassName}Model"
        val template = Files.newBufferedReader(templatePath).use {
          TemplateParser.parse(it, modelClassName)
        }
        
        // generate model class
        file(path(ClassName(cfg.pckg, modelClassName))) {
          write { os ->
            ModelsCodeGen.generate(cfg.pckg, template.model, os)
          }
          println("file generated '${path.fileName}' ('${path}')")
        }
        
        // generate renderer class
        val rendererClassName = "${baseClassName}Template"
        file(path(ClassName(cfg.pckg, rendererClassName))) {
          write { os ->
            RendererCodeGen.generate(cfg.pckg, rendererClassName, template, os)
          }
          println("file generated '${path.fileName}' ('${path}')")
        }
      }
    }
  }
}