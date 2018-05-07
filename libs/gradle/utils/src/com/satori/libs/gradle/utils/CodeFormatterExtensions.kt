package com.satori.libs.gradle.utils

import com.satori.codegen.utils.*

class CodeFormatterExtensions {
  companion object {
    @JvmStatic
    fun camelCase(self: String?) = self?.camelCase()
  
    @JvmStatic
    fun pascalCase(self: String?) = self?.pascalCase()
  
    @JvmStatic
    fun snakeCase(self: String?) = self?.snakeCase()
  
    @JvmStatic
    fun allCapsCase(self: String?) = self?.snakeCase(true)
  
    @JvmStatic
    fun kebabCase(self: String?) = self?.kebabCase()
  
    @JvmStatic
    fun dotsCase(self: String?) = self?.dotsCase()
  }
}
