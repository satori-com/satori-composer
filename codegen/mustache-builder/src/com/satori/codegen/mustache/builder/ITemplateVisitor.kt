package com.satori.codegen.mustache.builder

import com.github.mustachejava.*

interface ITemplateVisitor {
  fun append(text: String)
  fun opWrite(code: Code): ITemplateVisitor?
  fun opVarValue(varName: String): ITemplateVisitor?
  fun opIf(path: String): ITemplateVisitor?
  fun opIfNot(varName: String): ITemplateVisitor?
  fun opIfEmpty(varName: String): ITemplateVisitor?
  fun opIfNull(varName: String): ITemplateVisitor?
  fun opIfNotNull(varName: String): ITemplateVisitor?
  fun opFor(varName: String): ITemplateVisitor?
  fun opPartial(varName: String): ITemplateVisitor?
  fun opUnknown(code: Code): ITemplateVisitor?
}


