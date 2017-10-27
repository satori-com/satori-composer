package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public class JConditionalBuilder extends JBuilder implements JBlockTrait {
  public final JConditional $context;
  
  public JConditionalBuilder(JModelScript script, JConditional cond) {
    super(script);
    this.$context = cond;
  }
  
  @Override
  public JBlock getBlock() {
    return $context._then();
  }
  
  public void THEN(Closure cl) throws Exception {
    exec(new JBlockBuilder(getScript(),
      $context._then()
    ), cl);
  }
  
  public void ELSE(Closure cl) throws Exception {
    exec(new JBlockBuilder(getScript(),
      $context._else()
    ), cl);
  }
}