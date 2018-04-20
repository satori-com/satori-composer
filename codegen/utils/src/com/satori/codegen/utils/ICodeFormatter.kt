package com.satori.codegen.utils

interface ICodeFormatter {
  fun constName(name: String) = name.underscore(true)
  fun fieldName(name: String) = name.camel()
  fun methodName(name: String) = name.camel()
  fun varName(name: String) = name.camel()
  fun className(name: String) = name.pascal()
  fun getterName(name: String) = "get${name.pascal()}"
  fun setterName(name: String) = "set${name.pascal()}"
  fun camel(name: String) = name.camel()
  fun pascal(name: String) = name.pascal()
  fun underscore(name: String) = name.underscore(false)
  fun underscore(name: String, uppercase: Boolean) = name.underscore(uppercase)
  //fun elvis(name: String)
}
