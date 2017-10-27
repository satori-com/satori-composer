package com.satori.codegen.jmodel.codemodel.traits;

import com.satori.codegen.jmodel.codemodel.builders.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public interface JAnnotatableTrait extends JBuilderTrait {
  JAnnotatable getAnnotatable();
  
  default void ANNOTATION(JClass cls, Closure cl) throws Exception {
    exec(new JAnnotationBuilder(
      getScript(), getAnnotatable().annotate(cls)
    ), cl);
  }
  
  default void ANNOTATION(Class cls) throws Exception {
    ANNOTATION(CREF(cls), null);
  }
  
  default void ANNOTATION(String name, Closure cl) throws Exception {
    ANNOTATION(CREF(name), cl);
  }
  
  default void ANNOTATION(String name) throws Exception {
    ANNOTATION(name, null);
  }
}