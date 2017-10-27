package com.satori.codegen.jmodel.codemodel.traits;

import com.satori.codegen.jmodel.codemodel.builders.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public interface JBlockTrait extends JBuilderTrait {
  
  JBlock getBlock();
  
  default void CODE(String code) {
    getBlock().directStatement(code);
  }
  
  default void RETURN() {
    getBlock()._return();
  }
  
  default void RETURN(Object var) {
    getBlock()._return($LIT(var));
  }
  
  
  default void RETURN(JExpression exp) {
    getBlock()._return(exp);
  }
  
  default void RETURN(Closure cl) {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).asExpression();
    getBlock()._return(exp);
  }
  
  default JInvocation CALL(String name, Closure cl) {
    return exec(new JInvocationBuilder(getScript(),
      getBlock().invoke(name)
    ), cl).$context;
  }
  
  default JVar DECL(String name, JType type, Closure cl) {
    return exec(new JVarBuilder(getScript(),
      getBlock().decl(type, name)
    ), cl).$context;
  }
  
  default JVar DECL(String name, JType type) {
    return DECL(name, type, null);
  }
  
  default JVar DECL(String name, Class type, Closure cl) {
    return DECL(name, TREF(type), cl);
  }
  
  default JVar DECL(String name, Class type) {
    return DECL(name, type, null);
  }
  
  default JVar DECL(String name, String type, Closure cl) throws Exception {
    return DECL(name, TREF(type), cl);
  }
  
  default JVar DECL(String name, String type) throws Exception {
    return DECL(name, type, null);
  }
  
  default JVar DECL(String name, Closure cl) {
    return DECL(name, Object.class, cl);
  }
  
  default JVar DECL(String name) {
    return DECL(name, (Closure) null);
  }
  
  default void SWITCH(JExpression test, Closure cl) {
    exec(new JSwitchBuilder(getScript(),
      getBlock()._switch(test)
    ), cl);
  }
  
  default void IF(JExpression test, Closure cl) {
    exec(new JConditionalBuilder(getScript(),
      getBlock()._if(test)
    ), cl);
  }
  
  default void ASSIGN(JAssignmentTarget l, JExpression r) {
    getBlock().assign(l, r);
  }
  
  default void ASSIGN(JAssignmentTarget l, Closure cl) {
    ASSIGN(
      l, exec(new JExpressionListBuilder(getScript()), cl).asExpression()
    );
  }
  
  default void BREAK() {
    getBlock()._break();
  }
  
}