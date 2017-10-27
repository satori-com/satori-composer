package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public class JSwitchBuilder extends JBuilder {
  public final JSwitch $context;
  
  public JSwitchBuilder(JModelScript script, JSwitch sw) {
    super(script);
    this.$context = sw;
  }
  
  public void CASE(Object label, Closure cl) throws Exception {
    exec(new JBlockBuilder(getScript(),
      $context._case($LIT(label)).body()
    ), cl);
  }
  
  public void CASE(JExpression expr, Closure cl) throws Exception {
    exec(new JBlockBuilder(getScript(),
      $context._case(expr).body()
    ), cl);
  }
  
  public void DEFAULT(Closure cl) throws Exception {
    exec(new JBlockBuilder(getScript(),
      $context._default().body()
    ), cl);
  }
  
}