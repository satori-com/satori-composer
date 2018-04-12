package com.satori.codegen.mustache.builder

import com.github.mustachejava.*
import com.github.mustachejava.codes.*

private val appendedField = DefaultCode::class.java.getDeclaredField("appended").apply {
  isAccessible = true
}

fun DefaultCode._getAppended(): String? {
  return appendedField.get(this) as String?
}

private val mustacheField = DefaultCode::class.java.getDeclaredField("mustache").apply {
  isAccessible = true
}

fun DefaultCode._getMustache(): Mustache? {
  return mustacheField.get(this) as Mustache?
}

private val ohField = DefaultCode::class.java.getDeclaredField("oh").apply {
  isAccessible = true
}

fun DefaultCode._getOh(): ObjectHandler? {
  return ohField.get(this) as ObjectHandler?
}

private val nameField = DefaultCode::class.java.getDeclaredField("name").apply {
  isAccessible = true
}

fun DefaultCode._getName(): String? {
  return nameField.get(this) as String?
}

private val tcField = DefaultCode::class.java.getDeclaredField("tc").apply {
  isAccessible = true
}

fun DefaultCode._getTc(): TemplateContext? {
  return tcField.get(this) as TemplateContext?
}

private val typeField = DefaultCode::class.java.getDeclaredField("type").apply {
  isAccessible = true
}

fun DefaultCode._getType(): String? {
  return typeField.get(this) as String?
}

private val returnThisField = DefaultCode::class.java.getDeclaredField("returnThis").apply {
  isAccessible = true
}

fun DefaultCode._getReturnThis(): Boolean? {
  return returnThisField.get(this) as Boolean?
}

private val bindingField = DefaultCode::class.java.getDeclaredField("binding").apply {
  isAccessible = true
}

fun DefaultCode._getBinding(): Binding? {
  return bindingField.get(this) as Binding?
}

private val dfField = DefaultCode::class.java.getDeclaredField("df").apply {
  isAccessible = true
}

fun DefaultCode._getDf(): DefaultMustacheFactory? {
  return dfField.get(this) as DefaultMustacheFactory?
}

private val appendedCharsField = DefaultCode::class.java.getDeclaredField("appendedChars").apply {
  isAccessible = true
}

fun DefaultCode._getAppendedChars(): CharArray? {
  return appendedCharsField.get(this) as CharArray?
}