package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import java.lang.reflect.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public class JMethodBuilder extends JBuilder implements JBlockTrait {
  public final JMethod $context;
  
  @Override
  public JBlock getBlock() {
    return $context.body();
  }
  
  public JMethodBuilder(JModelScript script, JMethod method) {
    super(script);
    this.$context = method;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return $context;
  }
  
  @Override
  public JDocCommentable getDocCommentable() {
    return $context;
  }
  
  public JVar PARAM(String name, JType type, Closure cl) {
    return exec(new JVarBuilder(
      script, $context.param(type, name)
    ), cl).$context;
  }
  
  public JVar PARAM(String name, JType type) {
    return PARAM(name, type, null);
  }
  
  public JVar PARAM(String name, Closure cl) {
    return PARAM(name, TREF(Object.class), cl);
  }
  
  public JVar PARAM(String name, String type, Closure cl) throws Exception {
    return PARAM(name, TREF(type), cl);
  }
  
  public JVar PARAM(String name, String type) throws Exception {
    return PARAM(name, type, null);
  }
  
  public JVar PARAM(String name, Class type, Closure cl) {
    return PARAM(name, TREF(type), cl);
  }
  
  public JVar PARAM(String name, Class type) {
    return PARAM(name, TREF(type), null);
  }
  
  
  public void TYPE(JType type) throws Exception {
    $context.type(type);
  }
  
  public void TYPE(String type) throws Exception {
    TYPE(TREF(type));
  }
  
  public void TYPE(Class type) throws Exception {
    TYPE(TREF(type));
  }
  
  public void BODY(Closure cl) {
    exec(new JBlockBuilder(
      script, $context.body()
    ), cl);
  }
  
  public void STATIC() throws Exception {
    Method setFlag = JMods.class.getDeclaredMethod("setFlag", int.class, boolean.class);
    setFlag.setAccessible(true);
    Object r = setFlag.invoke($context.mods(), JMod.STATIC, true);
  }
}