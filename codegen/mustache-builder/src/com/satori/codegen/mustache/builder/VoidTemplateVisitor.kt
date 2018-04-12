package com.satori.codegen.mustache.builder

import com.github.mustachejava.*

object VoidTemplateVisitor : ITemplateVisitor {
  override fun append(text: String) {}
  override fun opWrite(code: Code) = this
  override fun opVarValue(varName: String) = this
  override fun opIf(varName: String) = this
  override fun opIfNot(varName: String) = this
  override fun opIfEmpty(varName: String) = this
  override fun opIfNull(varName: String) = this
  override fun opIfNotNull(varName: String) = this
  override fun opFor(varName: String) = this
  override fun opUnknown(code: Code) = this
  override fun opPartial(varName: String) = this
}
