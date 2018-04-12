package com.satori.codegen.utils

import java.io.*
import java.nio.file.*

class FileLib(val path: Path) {
  inline fun write(block: (OutputStream) -> Unit) {
    val options = arrayOf<OpenOption>(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    path.parent.toFile().mkdirs()
    Files.newOutputStream(path, *options).use {
      block(it)
    }
  }
  
  fun write(text: String) = write {
    it.writer().use {
      it.write(text)
    }
  }
  
  inline fun <reified T> read(block: (InputStream) -> T): T {
    Files.newInputStream(path).use {
      return block(it)
    }
  }
}

inline fun file(path: Path, block: FileLib.() -> Unit) {
  FileLib(path).block()
}

inline fun <reified T> file(path: String, block: FileLib.() -> T): T {
  return FileLib(Paths.get(path)).block()
}

inline fun <reified T> readFile(path: String, block: (InputStream) -> T): T {
  return file(path) {
    read(block)
  }
}

inline fun <reified T> writeFile(path: String, block: (OutputStream) -> Unit) {
  return file(path) {
    write(block)
  }
}
