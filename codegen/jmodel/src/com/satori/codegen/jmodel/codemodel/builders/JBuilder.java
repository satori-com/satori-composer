package com.satori.codegen.jmodel.codemodel.builders;

import com.satori.codegen.jmodel.codemodel.*;
import com.satori.codegen.jmodel.codemodel.traits.*;

import com.sun.codemodel.*;
import groovy.lang.*;

public class JBuilder extends GroovyObjectSupport implements
  JBuilderTrait,
  JAnnotatableTrait,
  JDocCommentableTrait,
  JClassContainerTrait {
  
  final JModelScript script;
  
  JBuilder(JModelScript script) {
    this.script = script;
  }
  
  @Override
  public JModelScript getScript() {
    return script;
  }
  
  @Override
  public JAnnotatable getAnnotatable() {
    return null;
  }
  
  @Override
  public JDocCommentable getDocCommentable() {
    return null;
  }
  
  @Override
  public JClassContainer getClassContainer() {
    return null;
  }
}