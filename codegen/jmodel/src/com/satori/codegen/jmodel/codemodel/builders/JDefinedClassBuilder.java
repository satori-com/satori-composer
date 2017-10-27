package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;

import com.sun.codemodel.*;
import groovy.lang.*;
import org.codehaus.groovy.runtime.*;

public class JDefinedClassBuilder extends JBuilder {
  public final JDefinedClass $context;
  
  public JDefinedClassBuilder(JModelScript script, JDefinedClass cls) {
    super(script);
    this.$context = cls;
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
  
  public JFieldVar FIELD(String name, JClass type, Closure cl) {
    return exec(new JFieldVarBuilder(script,
      $context.field(JMod.PUBLIC, type, name)
    ), cl).$context;
  }
  
  public JFieldVar FIELD(String name, JClass type) {
    return FIELD(name, type, null);
  }
  
  public JFieldVar FIELD(String name, Closure cl) {
    return FIELD(name, CREF(Object.class), cl);
  }
  
  public JFieldVar FIELD(String name) {
    return FIELD(name, (Closure) null);
  }
  
  public void CTOR(Closure cl) {
    int mods = JMod.NONE;
    if ($context.getClassType() == ClassType.CLASS) {
      mods |= JMod.PUBLIC;
    }
    
    exec(new JMethodBuilder(script,
      $context.constructor(mods)
    ), cl);
  }
  
  public void METHOD(String name, Closure cl) {
    exec(new JMethodBuilder(script,
      $context.method(JMod.PUBLIC, Void.TYPE, name)
    ), cl);
  }
  
  public JEnumConstant OPTION(String name, Closure cl) {
    return exec(new JEnumConstantBuilder(script,
      $context.enumConstant(name)
    ), cl).$context;
  }
  
  public JEnumConstant OPTION(String name, Object... args) {
    JEnumConstant enumConst = $context.enumConstant(name);
    for (Object arg : args) {
      //enumConst.arg(JExpr.lit(arg));
      enumConst.arg((JExpression) InvokerHelper.invokeStaticMethod(
        JExpr.class, "lit", new Object[]{arg}
      ));
    }
    return enumConst;
  }
  
  public void EXTENDS(JClass type) {
    $context._extends(type);
  }
  
  public void EXTENDS(Class type) {
    $context._extends(CREF(type));
  }
  
  public void EXTENDS(String type) throws Exception {
    $context._extends(CREF(type));
  }
}