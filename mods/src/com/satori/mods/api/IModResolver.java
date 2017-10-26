package com.satori.mods.api;

import com.fasterxml.jackson.databind.*;

public interface IModResolver {
  IModFactory resolve(String modRef) throws Exception;
  
  default IMod create(String modRef, JsonNode config) throws Exception{
    final IModFactory factory = resolve(modRef);
    if(factory == null) {
      throw new Exception(String.format(
        "failed to resolve mod ref: '%s'", modRef
      ));
    }
    return factory.create(config);
  }
}
