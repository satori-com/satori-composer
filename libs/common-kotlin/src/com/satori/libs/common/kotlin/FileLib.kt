package com.satori.libs.common.kotlin

import java.io.*
import java.nio.file.*

inline fun<reified T> file(path: Path, block: FileScope.() -> T): T {
  return FileScope(path).block()
}

inline fun<reified T> file(path: String, block: FileScope.() -> T): T {
  return FileScope(Paths.get(path)).block()
}

inline fun <reified T> readFile(path: String, block: (InputStream) -> T): T {
  return file(path) {
    read(block)
  }
}

inline fun <reified T> writeFile(path: String, block: (OutputStream) -> T): T {
  return file(path) {
    write(block)
  }
}
