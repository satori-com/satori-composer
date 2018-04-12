package com.satori.codegen.yaml.file.merger

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppConfig {
  @JsonProperty("--in")
  var input: String = ""
  
  @JsonProperty("--out")
  var output: String = ""
}


