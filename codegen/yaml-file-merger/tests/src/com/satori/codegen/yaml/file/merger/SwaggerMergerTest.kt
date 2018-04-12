package com.satori.codegen.yaml.file.merger

import com.satori.codegen.utils.*
import org.junit.*
import org.junit.rules.*
import java.nio.file.*

class SwaggerMergerTest() {
  val specPath = Paths.get("swagger.yaml").toAbsolutePath().toString()
  val dirSpecPath = Paths.get("swagger.yaml").toAbsolutePath().toString()
  
  @Rule
  @JvmField
  val testDir = TemporaryFolder()
  
  @Test
  fun test() {
    App.main("--in", specPath, "--out", "${testDir.root}/swagger.yaml")
    readFile("${testDir.root}/swagger.yaml") { istream ->
      istream.copyTo(System.out)
      System.out.flush()
    }
  }
  
  @Test
  fun `directory loader test`() {
    App.main("--in", dirSpecPath, "--out", "${testDir.root}/swagger.yaml")
    readFile("${testDir.root}/swagger.yaml") { istream ->
      istream.copyTo(System.out)
      System.out.flush()
    }
  }
  
}