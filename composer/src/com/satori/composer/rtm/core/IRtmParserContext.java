package com.satori.composer.rtm.core;

import com.fasterxml.jackson.databind.*;

public interface IRtmParserContext extends IRtmContext {
  ObjectMapper mapper();
}
    