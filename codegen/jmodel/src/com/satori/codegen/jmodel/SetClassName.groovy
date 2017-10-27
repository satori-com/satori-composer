package com.satori.codegen.jmodel

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.classgen.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.control.customizers.*

class SetClassName extends CompilationCustomizer {
  final String nameToSet;

  SetClassName(String nameToSet) {
    super(CompilePhase.CANONICALIZATION)
    this.nameToSet = nameToSet;
  }

  @Override
  void call(
    final SourceUnit source,
    final GeneratorContext context, final ClassNode classNode) throws CompilationFailedException {
    if (classNode.isScriptBody()) {
      classNode.setName(nameToSet)
    }
  }
}
