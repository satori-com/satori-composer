package com.satori.libs.gradle.utils

class CodeFormatterExtensions {
  companion object {
    @JvmStatic
    fun toCamel(self: String?) = CodeFormatterUtils.toCamel(self)
  
    @JvmStatic
    fun toPascal(self: String?) = CodeFormatterUtils.toPascal(self)
  }
}
