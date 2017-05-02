package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class XsltModSettings extends Config {
  
  @JsonProperty("xslt")
  public String xslt = null;
  
  public XsltModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
  }
}
