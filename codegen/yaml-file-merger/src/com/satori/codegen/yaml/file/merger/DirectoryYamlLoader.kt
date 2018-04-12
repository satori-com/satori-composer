package com.satori.codegen.yaml.file.merger

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.dataformat.yaml.*
import java.net.*
import java.nio.charset.*
import java.nio.file.*

object DirectoryYamlLoader {
  val mapper = YAMLMapper()
  
  fun load(path: Path): JsonNode {
    val file = path.toFile()
    if (file.isDirectory) return loadDirectory(path)
    return loadFile(path)
  }
  
  // private methods
  
  private fun loadDirectory(path: Path): JsonNode {
    val res = mapper.getNodeFactory().objectNode()
    for (file in path.toFile().listFiles()!!) {
      val nameWithExt = file.name
      val dotIdx = nameWithExt.lastIndexOf('.')
      if (dotIdx < 0) {
        continue
      }
      val ext = nameWithExt.substring(dotIdx + 1)
      val name: String? = nameWithExt.substring(0, dotIdx)
      if ("yaml" != ext || name == null || name.isEmpty()) {
        continue
      }
      if (name.startsWith("-")) {
        val appendObject = load(file.toPath()) as? ObjectNode ?: throw Exception(
          "only objects can be appended, but '$nameWithExt' is not"
        )
        for ((k, v) in appendObject.fields()) {
          if (res.get(k) != null) throw Exception(
            "failed to append '$k' value from '$file', due it already exists"
          )
          res.replace(k, v)
        }
      } else {
        val k = URLDecoder.decode(name, StandardCharsets.UTF_8.name())
        if (res.get(k) != null) throw Exception(
          "failed to append  file '$file', due this key already exists"
        )
        res.replace(k, load(file.toPath()))
      }
    }
    return res
  }
  
  private fun loadFile(path: Path): JsonNode {
    try {
      Files.newBufferedReader(path).use { reader ->
        return mapper.readTree(reader)
      }
    } catch (e: Throwable) {
      throw Exception(String.format(
        "failed to load '%s'", path.toAbsolutePath()
      ), e)
    }
    
  }
  
}
