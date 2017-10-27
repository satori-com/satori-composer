package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import com.sun.codemodel.*;

public class JBlockBuilder extends JBuilder implements JBlockTrait {
  public final JBlock $context;
  
  @Override
  public JBlock getBlock() {
    return $context;
  }
  
  public JBlockBuilder(JModelScript script, JBlock block) {
    super(script);
    this.$context = block;
  }
}