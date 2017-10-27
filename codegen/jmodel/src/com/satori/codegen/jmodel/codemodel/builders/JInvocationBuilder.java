package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public class JInvocationBuilder extends JBuilder implements JExpressionListTrait {
  public final JInvocation $context;
  
  public JInvocationBuilder(JModelScript script, JInvocation invocation) {
    super(script);
    this.$context = invocation;
  }
  
  public JInvocationBuilder(JModelScript context, String name) {
    this(context, JExpr.invoke(name));
  }
  
  @Override
  public void add(JExpression exp) {
    $context.arg(exp);
  }
  
  public void ARG(JExpression arg) throws Exception {
    add(arg);
  }
  
  public void ARG(Object arg) throws Exception {
    ARG($LIT(arg));
  }
  
  public void ARG(Closure cl) throws Exception {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).asExpression();
    ARG(exp);
  }
  
}