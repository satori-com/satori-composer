package com.satori.libs.common.kotlin

import java.io.*
import java.nio.file.*

class FileScope(val path: Path) {
  
  inline fun<reified T> write(block: (OutputStream) -> T):T {
    val options = arrayOf<OpenOption>(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    path.parent?.toFile()?.mkdirs()
    Files.newOutputStream(path, *options).use {
      return block(it)
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
