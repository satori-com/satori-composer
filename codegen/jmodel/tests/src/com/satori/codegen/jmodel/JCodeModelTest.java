package com.satori.codegen.jmodel;

import java.io.*;

import com.sun.codemodel.*;

public class JCodeModelTest {
  public static void main(String... args) throws Exception {
    JCodeModel jmodel = new JCodeModel();
    JPackage pckg = jmodel._package("com.mz");
    do{
      JDefinedClass cls = pckg._class("Foo");
      JFieldVar x = cls.field(JMod.PUBLIC, int.class, "x");
  
      JClass jsonValueClass = jmodel.directClass("com.fasterxml.jackson.annotation.JsonValue");
      
      x.annotate(jmodel.ref("com1.fasterxml.jackson.annotation.JsonValue"));
      JFieldVar y = cls.field(JMod.PUBLIC, int.class, "y");
      y.annotate(jmodel.ref("com1.fasterxml.jackson.annotation.JsonValue"));
    
    }while (false);
    
    jmodel.build(new CodeWriter() {
      @Override
      public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        return System.out;
      }
    
      @Override
      public void close() throws IOException {
      
      }
    });
    System.out.flush();
  }
}
