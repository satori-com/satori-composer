package com.satori.libs.gradle.utils

class CodeFormatterUtils {
  companion object {
    @JvmStatic
    fun toCamel(str: String?): String? {
      str ?: return null
      var capitalize = true
      val builder = StringBuilder()
      for (i in 0 until str.length) {
        var ch = str[i]
        
        if (Character.isDigit(ch)) {
          capitalize = true;
          if (builder.length > 0) {
            builder.append(ch)
          }
          continue
        }
        
        if (!Character.isAlphabetic(ch.toInt())) {
          capitalize = true;
          continue;
        }
        
        if (builder.length <= 0) {
          ch = Character.toLowerCase(ch)
        } else if (capitalize) {
          ch = Character.toUpperCase(ch)
        }
        capitalize = false
        builder.append(ch)
      }
      return builder.toString();
    }
  
    @JvmStatic
    fun toPascal(str: String?): String? {
      str ?: return null
      var capitalize = true
      val builder = StringBuilder()
      for (i in 0 until str.length) {
        var ch = str[i]
      
        if (Character.isDigit(ch)) {
          capitalize = true
          if (builder.length > 0) {
            builder.append(ch)
          }
          continue
        }
      
        if (!Character.isAlphabetic(ch.toInt())) {
          capitalize = true
          continue
        }
      
        if (capitalize) {
          ch = Character.toUpperCase(ch)
        }
        capitalize = false
        builder.append(ch)
      }
      return builder.toString()
    }
  }
}
