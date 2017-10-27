package com.satori.codegen.jmodel.codemodel.traits;

import com.sun.codemodel.*;

public interface JDocCommentableTrait {
  JDocCommentable getDocCommentable();
  
  default void COMMENT(String text) {
    if (text == null || text.isEmpty()) {
      return;
    }
    JDocComment comments = getDocCommentable().javadoc();
    comments.append(text.replace("\r\n", "\n"));
  }
}