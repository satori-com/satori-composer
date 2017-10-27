package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;
import com.sun.codemodel.*;
import org.codehaus.groovy.runtime.*;

public class JAnnotationBuilder extends JBuilder implements JExpressionListTrait {
  public final JAnnotationUse $context;
  
  public JAnnotationBuilder(JModelScript script, JAnnotationUse annotation) {
    super(script);
    this.$context = annotation;
  }
  
  @Override
  public void add(JExpression exp) {
    $context.param("value", exp);
  }
  
  public void PARAM(Object value) {
    //annotation.param("value", value);
    InvokerHelper.invokeMethod($context, "param", new Object[]{"value", value});
  }
}