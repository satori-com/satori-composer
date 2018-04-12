package com.satori.codegen.utils

val javaKeywords = hashSetOf(
  "abstract", "continue", "for", "new", "switch",
  "assert", "default", "if", "package", "synchronized",
  "boolean", "do", "goto", "private", "this",
  "break", "double", "implements", "protected", "throw",
  "byte", "else", "import", "public", "throws",
  "case", "enum", "instanceof", "return", "transient",
  "catch", "extends", "int", "short", "try",
  "char", "final", "interface", "static", "void",
  "class", "finally", "long", "strictfp", "volatile",
  "const", "float", "native", "super", "while"
)

fun String.pascal(): String = let { str ->
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

fun String.camel(): String = let { str ->
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
    
    if (builder.length <= 0) {
      ch = Character.toLowerCase(ch)
    } else if (capitalize) {
      ch = Character.toUpperCase(ch)
    }
    capitalize = false
    builder.append(ch)
  }
  return builder.toString()
}

fun String.underscore(uppercase: Boolean = false): String {
  var underScoreNeeded = false
  val builder = StringBuilder()
  forEach { ch ->
    when (Character.getType(ch).toByte()) {
      Character.DECIMAL_DIGIT_NUMBER -> {
        if (!builder.isEmpty()) {
          if (underScoreNeeded) {
            builder.append("_")
            underScoreNeeded = false
          }
          builder.append(ch)
        }
      }
      Character.LOWERCASE_LETTER -> {
        if (underScoreNeeded) {
          builder.append("_")
          underScoreNeeded = false
        }
        builder.append(
          if (uppercase) ch.toUpperCase() else ch
        )
      }
      Character.UPPERCASE_LETTER -> {
        if (!builder.isEmpty()) {
          builder.append("_")
        }
        underScoreNeeded = false
        builder.append(
          if (!uppercase) ch.toLowerCase() else ch
        )
      }
      else -> {
        if (!builder.isEmpty()) {
          underScoreNeeded = true
        }
      }
    }
  }
  return builder.toString()
}