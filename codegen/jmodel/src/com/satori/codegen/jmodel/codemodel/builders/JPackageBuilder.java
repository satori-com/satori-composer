package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import com.sun.codemodel.*;

public class JPackageBuilder extends JBuilder {
  public final JPackage $context;
  
  public JPackageBuilder(JModelScript script, JPackage pckg) {
    super(script);
    this.$context = pckg;
  }
  
  @Override
  public JDocCommentable getDocCommentable() {
    return $context;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return $context;
  }
  
  @Override
  public JClassContainer getClassContainer() {
    return $context;
  }
}