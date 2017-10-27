package com.satori.codegen.jmodel.codemodel.traits;

import com.satori.codegen.jmodel.codemodel.builders.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public interface JExpressionListTrait extends JBuilderTrait {
  
  void add(JExpression exp);
  
  default void CALL(String name, Closure cl) {
    add(
      exec(new JInvocationBuilder(
        getScript(), name
      ), cl).$context
    );
  }
  
  default void CALL(Class type, String name, Closure cl) {
    CALL(CREF(type), name, cl);
  }
  
  default void CALL(JClass type, String name, Closure cl) {
    add(
      exec(new JInvocationBuilder(
        getScript(), type.staticInvoke(name)
      ), cl).$context
    );
  }
  
  default JExpression AND(Closure cl) {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).$context.stream().reduce(null,
      (acc, e) -> acc != null ? JOp.cand(acc, e) : e
    );
    if (exp != null) {
      add(exp);
    }
    return exp;
  }
  
  default JExpression EXP(String code) {
    JExpression exp = JExpr.direct(code);
    add(exp);
    return exp;
  }
  
  default JExpression EXP(JExpression exp) {
    add(exp);
    return exp;
  }
  
  default JExpression LIT(Object val) {
    JExpression exp = $LIT(val);
    add(exp);
    return exp;
  }
  
  default JExpression OR(Closure cl) {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).$context.stream().reduce(null,
      (acc, e) -> acc != null ? JOp.cor(acc, e) : e
    );
    if (exp != null) {
      add(exp);
    }
    return exp;
  }
  
  default JExpression SUM(Closure cl) {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).$context.stream().reduce(null,
      (acc, e) -> acc != null ? JOp.plus(acc, e) : e
    );
    if (exp != null) {
      add(exp);
    }
    return exp;
  }
  
  default JExpression EQ(Closure cl) {
    JExpression exp = exec(new JExpressionListBuilder(
      getScript()
    ), cl).$context.stream().reduce(null,
      (acc, e) -> acc != null ? JOp.eq(acc, e) : e
    );
    if (exp != null) {
      add(exp);
    }
    return exp;
  }
  
  default JExpression CAST(JExpression exp, JType type) {
    if (exp == null) {
      throw new NullPointerException();
    }
    JExpression res = JExpr.cast(type, exp);
    add(res);
    return res;
  }
  
  default JExpression FIELD(JExpression exp, String name) {
    if (exp == null) {
      throw new NullPointerException();
    }
    JExpression res = JExpr.ref(exp, name);
    add(res);
    return res;
  }
  
  default JExpression FIELD(JExpression exp, JVar name) {
    if (exp == null) {
      throw new NullPointerException();
    }
    JExpression res = JExpr.ref(exp, name);
    add(res);
    return res;
  }
  
  default JExpression FIELD(String name) {
    JExpression res = JExpr.ref(name);
    add(res);
    return res;
  }
  
  default JExpression FIELD(JClass type, String name) {
    JExpression res = type.staticRef(name);
    add(res);
    return res;
  }
  
  default JExpression FIELD(Class type, String name) {
    return FIELD(CREF(type), name);
  }
  
  default JExpression THIS(String name) {
    JExpression res = JExpr.refthis(name);
    add(res);
    return res;
  }
  
  default JInvocation NEW(JType type, Closure cl) {
    JInvocation res = exec(new JInvocationBuilder(
      getScript(), JExpr._new(type)
    ), cl).$context;
    add(res);
    return res;
  }
  
  default JInvocation NEW(JType type) {
    return NEW(type, (Closure) null);
  }
  
  default JInvocation NEW(Class type, Closure cl) {
    return NEW(TREF(type), cl);
  }
  
  default JInvocation NEW(Class type) {
    return NEW(type, (Closure) null);
  }
  
  default JInvocation NEW(String type, Closure cl) throws Exception {
    return NEW(TREF(type), cl);
  }
  
  default JInvocation NEW(String type) throws Exception {
    return NEW(type, (Closure) null);
  }
  
  default JArray NEW_ARRAY(JType type, int size) {
    JArray res = JExpr.newArray(type, size);
    add(res);
    return res;
  }
  
  default JArray NEW_ARRAY(Class type, int size) {
    return NEW_ARRAY(TREF(type), size);
  }
  
  default JArray NEW_ARRAY(String type, int size) throws Exception {
    return NEW_ARRAY(TREF(type), size);
  }
  
}