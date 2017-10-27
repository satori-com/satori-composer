package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import java.util.*;

import com.sun.codemodel.*;

public class JExpressionListBuilder extends JBuilder implements JExpressionListTrait {
  public final ArrayList<JExpression> $context = new ArrayList<>();
  
  public JExpressionListBuilder(JModelScript script) {
    super(script);
  }
  
  @Override
  public void add(JExpression exp) {
    $context.add(exp);
  }
  
  public JExpression asExpression() {
    return new JExpressionImpl() {
      @Override
      public void generate(JFormatter f) {
        boolean first = true;
        for (JExpression e : $context) {
          if (!first) {
            f.p(" ,");
          }
          e.generate(f);
          first = false;
        }
      }
    };
  }
}