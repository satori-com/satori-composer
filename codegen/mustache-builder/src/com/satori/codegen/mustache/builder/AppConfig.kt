package com.satori.codegen.mustache.builder

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppConfig {
  @JsonProperty("--pckg")
  var pckg: String = ""
  
  @JsonProperty("--templates")
  var templates: String? = null
  
  @JsonProperty("--out")
  var out: String = ""
  
  @JsonProperty("--prefix")
  var prefix: String = ""
}


