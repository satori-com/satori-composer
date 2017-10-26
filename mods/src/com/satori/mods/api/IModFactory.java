package com.satori.mods.api;

import com.fasterxml.jackson.databind.*;

public interface IModFactory {
  IMod create(JsonNode config) throws Exception;
}
