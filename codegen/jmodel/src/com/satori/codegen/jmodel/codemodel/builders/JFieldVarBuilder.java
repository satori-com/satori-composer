package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import com.sun.codemodel.*;

public class JFieldVarBuilder extends JBuilder {
  public final JFieldVar $context;
  
  public JFieldVarBuilder(JModelScript script, JFieldVar fieldVar) {
    super(script);
    this.$context = fieldVar;
  }
  
  @Override
  public JDocCommentable getDocCommentable() {
    return $context;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return $context;
  }
  
  public void TYPE(String name) throws Exception {
    $context.type(TREF(name));
  }
}
