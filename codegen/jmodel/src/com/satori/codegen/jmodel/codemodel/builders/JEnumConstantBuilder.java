package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import com.sun.codemodel.*;
import org.codehaus.groovy.runtime.*;

public class JEnumConstantBuilder extends JBuilder {
  public final JEnumConstant $context;
  
  public JEnumConstantBuilder(JModelScript script, JEnumConstant enumConst) {
    super(script);
    this.$context = enumConst;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return $context;
  }
  
  @Override
  public JDocCommentable getDocCommentable() {
    return $context;
  }
  
  public void ARG(Object... args) {
    for (Object arg : args) {
      //enumConst.arg(JExpr.lit(arg));
      $context.arg((JExpression) InvokerHelper.invokeStaticMethod(
        JExpr.class, "lit", new Object[]{arg}
      ));
    }
  }
  
}