package com.satori.codegen.mustache.builder

import com.github.mustachejava.*
import java.util.logging.*

private val loggerField = DefaultMustacheVisitor::class.java.getDeclaredField("logger").apply {
  isAccessible = true
}

fun DefaultMustacheVisitor._getLogger(): Logger? {
  return loggerField.get(this) as Logger?
}

private val EOFField = DefaultMustacheVisitor::class.java.getDeclaredField("EOF").apply {
  isAccessible = true
}

fun DefaultMustacheVisitor?._getEOF(): Code? {
  return EOFField.get(this) as Code?
}

private val listField = DefaultMustacheVisitor::class.java.getDeclaredField("list").apply {
  isAccessible = true
}

private val handlersField = DefaultMustacheVisitor::class.java.getDeclaredField("handlers").apply {
  isAccessible = true
}

private val dfField = DefaultMustacheVisitor::class.java.getDeclaredField("df").apply {
  isAccessible = true
}

fun DefaultMustacheVisitor._getDf(): DefaultMustacheFactory? {
  return dfField.get(this) as DefaultMustacheFactory?
}