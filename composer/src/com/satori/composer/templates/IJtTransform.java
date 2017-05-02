package com.satori.composer.templates;

import com.fasterxml.jackson.databind.*;
import freemarker.template.*;

public interface IJtTransform<T> {
  T transform(TemplateModel ctx) throws Exception;
  
  default T transform(JsonNode ctx) throws Exception {
    return transform(JtModel.wrap(ctx));
  }
}