package com.satori.codegen.jschema.models.gen

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppConfig {
  @JsonProperty("--pckg")
  var pckg: String = ""
  
  @JsonProperty("--schema")
  var schema: String? = null
  
  @JsonProperty("--out")
  var out: String = ""
  
  @JsonProperty("--name")
  var name: String? = null
  
  @JsonProperty("--prefix")
  var prefix: String = ""
}


