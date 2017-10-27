package com.satori.codegen.jmodel.codemodel;

import com.satori.codegen.jmodel.codemodel.builders.*;

import java.io.*;
import java.util.*;

import com.sun.codemodel.*;
import groovy.lang.*;
import org.codehaus.groovy.runtime.*;


public abstract class JModelScript extends Script {
  
  public final static JExpression NULL = JExpr._null();
  public final static JExpression TRUE = JExpr.TRUE;
  public final static JExpression FALSE = JExpr.FALSE;
  public final static JExpression THIS = JExpr._this();
  
  public JCodeModel model;
  public JBuilder builder;
  HashMap<String, JClass> crefs = new HashMap<>();
  
  public JModelScript() {
    model = new JCodeModel();
    builder = new JCodeModelBuilder(this);
  }
  
  public JCodeModel getModel() {
    return model;
  }
  
  public JClass CREF(String name) {
    return crefs.computeIfAbsent(name.trim(), k -> {
      try {
        return (JClass) model.parseType(k);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      
    });
  }
  
  public JClass CREF(Class cls) {
    return CREF(cls.getName());
  }
  
  public JType TREF(Class cls) {
    return model._ref(cls);
  }
  
  public JType TREF(String type) throws Exception {
    return model.parseType(type);
  }
  
  public JExpression $LIT(Object val) {
    return (JExpression) InvokerHelper.invokeStaticMethod(
      JExpr.class, "lit", new Object[]{val}
    );
  }
  
  public JExpression $EQ(JExpression l, JExpression r) {
    return JOp.eq(l, r);
  }
  
  public JExpression $OR(JExpression l, JExpression r) {
    return JOp.cor(l, r);
  }
  
  public JExpression $NOT(JExpression e) {
    return JOp.not(e);
  }
  
  public JExpression $CALL(JExpression lhs, String method, Closure cl) {
    return exec(new JInvocationBuilder(this,
      JExpr.invoke(lhs, method)
    ), cl).$context;
  }
  
  public JExpression $CALL(Class type, String name, Closure cl) {
    return $CALL(CREF(type), name, cl);
  }
  
  public JExpression $CALL(JClass type, String name, Closure cl) {
    return exec(new JInvocationBuilder(
      this, type.staticInvoke(name)
    ), cl).$context;
  }
  
  public JExpression $EXP(String code) {
    return JExpr.direct(code);
  }
  
  public JFieldRef $FIELD(JExpression lhs, String field) {
    if (lhs == null) {
      throw new NullPointerException();
    }
    return JExpr.ref(lhs, field);
  }
  
  public JFieldRef $FIELD(JExpression lhs, JVar field) {
    if (lhs == null) {
      throw new NullPointerException();
    }
    return JExpr.ref(lhs, field);
  }
  
  public JFieldRef $FIELD(String field) {
    return JExpr.ref(field);
  }
  
  public JFieldRef $THIS(String field) {
    return JExpr.refthis(field);
  }
  
  /*@Override
  public Object invokeMethod(String name, Object args) {
    return super.invokeMethod(name, args);
  }*/
  
  public Object methodMissing(String name, Object args) throws IOException {
    return builder.invokeMethod(name, args);
  }
  
  public static String toCamel(String str) {
    if (str == null) {
      return null;
    }
    boolean capitalize = true;
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      
      if (Character.isDigit(ch)) {
        capitalize = true;
        if (builder.length() > 0) {
          builder.append(ch);
        }
        continue;
      }
      
      if (!Character.isLetter(ch)) {
        capitalize = true;
        continue;
      }
      
      if (builder.length() <= 0) {
        ch = Character.toLowerCase(ch);
      } else if (capitalize) {
        ch = Character.toUpperCase(ch);
      }
      capitalize = false;
      builder.append(ch);
    }
    return builder.toString();
  }
  
  public static String toPascal(String str) {
    if (str == null) {
      return null;
    }
    boolean capitalize = true;
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      
      if (Character.isDigit(ch)) {
        capitalize = true;
        if (builder.length() > 0) {
          builder.append(ch);
        }
        continue;
      }
      
      if (!Character.isLetter(ch)) {
        capitalize = true;
        continue;
      }
      
      if (capitalize) {
        ch = Character.toUpperCase(ch);
      }
      capitalize = false;
      builder.append(ch);
    }
    return builder.toString();
  }
  
  public <T extends JBuilder> T exec(T builder, Closure cl) {
    if (cl == null) {
      return builder;
    }
    JBuilder prev = this.builder;
    this.builder = builder;
    try {
      cl.call();
    } finally {
      this.builder = prev;
    }
    return builder;
  }
  
  public void exec(Closure cl) {
    cl.setDelegate(this);
    cl.setResolveStrategy(Closure.DELEGATE_FIRST);
    cl.call();
  }
  
}

