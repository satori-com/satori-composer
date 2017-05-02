package com.satori.composer.templates;

import java.util.*;

import freemarker.template.*;

public class JtString implements TemplateScalarModel {
  final String val;
  
  public JtString(String val) {
    this.val = val;
  }
  
  @Override
  public String getAsString() throws TemplateModelException {
    return val;
  }
  
  @Override
  public String toString() {
    return val;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return val == null;
    return Objects.equals(val, o.toString());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(val);
  }
}
