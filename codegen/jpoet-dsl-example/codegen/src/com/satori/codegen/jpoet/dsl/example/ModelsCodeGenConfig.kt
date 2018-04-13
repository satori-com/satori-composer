package com.satori.codegen.jpoet.dsl.example

import java.nio.file.*

class ModelsCodeGenConfig() {
  var pckg: String = ""
  var outDir: Path = Paths.get("")
  var schemaPath: Path = Paths.get("schema.graphqls")
  var classPrefix: String = ""
}


