package com.satori.codegen.jmodel.codemodel.traits;

import com.sun.codemodel.*;

public interface JTypeTrait extends JBuilderTrait {
  
  void setType(JType type);
  
  JType getType();
  
  default void TYPE(JType type) throws Exception {
    setType(type);
  }
  
  default void TYPE(Class type) throws Exception {
    TYPE(TREF(type));
  }
  
  default void TYPE(String type) throws Exception {
    TYPE(TREF(type));
  }
}