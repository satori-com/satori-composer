package com.satori.codegen.jmodel.codemodel.traits;

import com.satori.codegen.jmodel.codemodel.builders.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public interface JClassContainerTrait extends JBuilderTrait {
  JClassContainer getClassContainer();
  
  default void CLASS(String name, Closure cl) throws Exception {
    int mods = JMod.PUBLIC;
    if (!getClassContainer().isPackage()) {
      mods |= JMod.STATIC;
    }
    exec(new JDefinedClassBuilder(getScript(),
      getClassContainer()._class(mods, name)
    ), cl);
  }
  
  default void ENUM(String name, Closure cl) throws Exception {
    exec(new JDefinedClassBuilder(getScript(),
      getClassContainer()._enum(name)
    ), cl);
  }
  
  default void INTERFACE(String name, Closure cl) throws Exception {
    exec(new JDefinedClassBuilder(getScript(),
      getClassContainer()._interface(JMod.PUBLIC, name)
    ), cl);
  }
}