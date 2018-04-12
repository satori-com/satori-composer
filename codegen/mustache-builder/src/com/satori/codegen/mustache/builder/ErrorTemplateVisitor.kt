package com.satori.codegen.mustache.builder

import com.github.mustachejava.*

object ErrorTemplateVisitor : ITemplateVisitor {
  override fun append(text: String) = throw Exception("")
  override fun opWrite(code: Code) = throw Exception("")
  override fun opVarValue(varName: String) = throw Exception("unexpected operation")
  override fun opIf(varName: String) = throw Exception("unexpected operation")
  override fun opIfNot(varName: String) = throw Exception("unexpected operation")
  override fun opIfEmpty(varName: String) = throw Exception("unexpected operation")
  override fun opIfNull(varName: String) = throw Exception("unexpected operation")
  override fun opIfNotNull(varName: String) = throw Exception("unexpected operation")
  override fun opFor(varName: String) = throw Exception("unexpected operation")
  override fun opUnknown(code: Code) = throw Exception("unexpected operation")
  override fun opPartial(varName: String) = throw Exception("unexpected operation")
}
