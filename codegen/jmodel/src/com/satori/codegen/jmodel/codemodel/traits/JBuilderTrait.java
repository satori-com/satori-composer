package com.satori.codegen.jmodel.codemodel.traits;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.builders.*;

import com.sun.codemodel.*;
import groovy.lang.*;


public interface JBuilderTrait {
  
  JModelScript getScript();
  
  
  default JCodeModel getModel() {
    return getScript().getModel();
  }
  
  default JClass CREF(String name) throws Exception {
    return getScript().CREF(name);
  }
  
  default JClass CREF(Class cls) {
    return getScript().CREF(cls);
  }
  
  default JType TREF(Class cls) {
    return getScript().TREF(cls);
  }
  
  default JType TREF(String type) throws Exception {
    return getScript().TREF(type);
  }
  
  default <T extends JBuilder> T exec(T builder, Closure cl) {
    return getScript().exec(builder, cl);
  }
  
  default JExpression $LIT(Object val) {
    return getScript().$LIT(val);
  }
  
  /*default JExpression EXP(String code) {
    return JExpr.direct(code);
  }*/
  
}