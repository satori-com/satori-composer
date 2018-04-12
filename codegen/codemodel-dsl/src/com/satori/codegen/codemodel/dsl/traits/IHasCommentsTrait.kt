package com.satori.codegen.codemodel.dsl.traits

import com.satori.codegen.codemodel.dsl.scopes.*
import com.sun.codemodel.*

interface IHasCommentsTrait : IHasComments {
  val hasCommentsTrait: JDocComment
  override fun COMMENT(line: String) {
    hasCommentsTrait.append(line)
  }
}
