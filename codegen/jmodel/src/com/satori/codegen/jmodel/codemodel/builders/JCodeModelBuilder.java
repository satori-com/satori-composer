package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import groovy.lang.*;

public class JCodeModelBuilder extends JBuilder {
  
  public JCodeModelBuilder(JModelScript script) {
    super(script);
  }
  
  public void PACKAGE(String name, Closure cl) {
    exec(new JPackageBuilder(script,
      getScript().getModel()._package(name)
    ), cl);
  }
}