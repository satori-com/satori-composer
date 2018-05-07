package com.satori.codegen.utils

interface ICodeFormatter {
  fun constName(name: String) = name.snakeCase(true)
  fun fieldName(name: String) = name.camelCase()
  fun methodName(name: String) = name.camelCase()
  fun varName(name: String) = name.camelCase()
  fun className(name: String) = name.pascalCase()
  fun getterName(name: String) = "get${name.pascalCase()}"
  fun setterName(name: String) = "set${name.pascalCase()}"
  fun packageName(name: String) = name.dotsCase()
  
  fun camel(name: String) = name.camelCase()
  fun pascal(name: String) = name.pascalCase()
  fun snake(name: String) = name.snakeCase(false)
  fun snake(name: String, uppercase: Boolean) = name.snakeCase(uppercase)
  fun kebab(name: String) = name.kebabCase(false)
  fun kebab(name: String, uppercase: Boolean) = name.kebabCase(uppercase)
}
