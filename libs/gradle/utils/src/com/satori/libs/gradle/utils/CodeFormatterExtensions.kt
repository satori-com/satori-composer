package com.satori.libs.gradle.utils

import com.satori.codegen.utils.*

class CodeFormatterExtensions {
  companion object {
    @JvmStatic
    fun toCamel(self: String?) = self?.camel()
  
    @JvmStatic
    fun toPascal(self: String?) = self?.pascal()
  }
}
