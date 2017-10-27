package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import com.sun.codemodel.*;

public class JVarBuilder extends JBuilder implements JTypeTrait, JExpressionListTrait {
  public final JVar $context;
  
  public JVarBuilder(JModelScript script, JVar var) {
    super(script);
    this.$context = var;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return $context;
  }
  
  @Override
  public void setType(JType type) {
    $context.type(type);
  }
  
  @Override
  public JType getType() {
    return $context.type();
  }
  
  @Override
  public void add(JExpression exp) {
    $context.init(exp);
  }
}