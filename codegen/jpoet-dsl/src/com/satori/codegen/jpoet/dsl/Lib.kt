package com.satori.codegen.jpoet.dsl

import com.satori.codegen.jpoet.dsl.builders.*
import com.satori.codegen.jpoet.dsl.scopes.*
import com.squareup.javapoet.*
import java.io.*
import java.nio.file.*

fun PACKAGE(name: String, configure: IPackageScope.() -> Unit) {
  PackageBuilder { typeSpec ->
    val javaFile = JavaFile.builder(name, typeSpec)
      .skipJavaLangImports(true)
      .addFileComment("""
          auto generated
          don't modify
        """.trimIndent())
      .build()
    javaFile.writeTo(System.out)
  }.configure()
}

fun CLASS(name: String, configure: TypeBuilder.() -> Unit): TypeSpec {
  val typeBuilder = TypeSpec.classBuilder(name)
  TypeBuilder(typeBuilder).configure()
  return typeBuilder.build()
}

fun FILE(os: OutputStream, configure: IFileScope.() -> Unit) {
  FileBuilder(os).configure()
}

fun FILE(path: Path, configure: IFileScope.() -> Unit) {
  val options = arrayOf<OpenOption>(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  path.parent.toFile().mkdirs()
  Files.newOutputStream(path, *options).use {
    FILE(it, configure)
  }
}

fun FILE(file: File, configure: IFileScope.() -> Unit) = FILE(file.toPath(), configure)

fun FILE(path: String, configure: IFileScope.() -> Unit) = FILE(Paths.get(path), configure)



