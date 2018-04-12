package com.satori.codegen.yaml.file.merger

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.json.*
import java.io.*
import java.net.*
import java.nio.file.*

class App(val specPath: Path) {
  val mapper = YAMLMapper()
  val basePath = specPath.parent.toString()
  
  fun process(writer: Writer) {
    val tree = process(DirectoryYamlLoader.load(specPath))
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, tree)
  }
  
  fun loadTree(path: String): JsonNode {
    return DirectoryYamlLoader.load(Paths.get(basePath, path))
  }
  
  fun process(tree: JsonNode?, path: String = "", rebase: String? = null): JsonNode? {
    when (tree) {
      null -> return null
      is ObjectNode -> {
        val ref = tree["\$ref"]?.let { URI(it.asText()) }
        if (ref == null) {
          tree.fields().forEach { (k, v) ->
            tree.replace(k, process(v, "$path/$k", rebase))
          }
          return tree
        }
        if (ref.path.isNullOrBlank()) {
          if (ref.fragment == null) {
            throw Exception("scheme '${ref.scheme}' is not supported")
          }
          if (ref.fragment.startsWith("/")) return tree
          if (rebase == null || rebase.isEmpty()) return tree
          tree.put("\$ref", "#$rebase/${ref.fragment}")
          return tree
        }
        if (ref.scheme != null) throw Exception("scheme '${ref.scheme}' is not supported")
        if (ref.fragment != null) throw Exception("file references with fragments are not supported")
        return process(loadTree(ref.path), path, path)
      }
      is ArrayNode -> {
        for (i in 0 until tree.size()) {
          tree[i] = process(tree[i], "$path/[$i]", rebase)
        }
        return tree
      }
      else -> return tree
    }
  }
  
  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      println(MetaInfo)
      
      val cfg = jsonObject {
        val itor = args.iterator()
        while (true) {
          val name = if (itor.hasNext()) itor.next() else break
          val value = if (itor.hasNext()) itor.next() else throw Exception("argument value is missing")
          if (!name.startsWith("--")) throw Exception("argument '$name' should start with '--'")
          field(name, value)
        }
      }.toValue<AppConfig>()
      
      file(cfg.output) {
        write { os ->
          App(Paths.get(cfg.input)).process(os.writer())
        }
      }
    }
  }
}