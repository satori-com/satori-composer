package com.satori.libs.composition.drawer

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppArgs {
  @JsonProperty("cfg-path")
  var cfgPath: String? = null
  
  @JsonProperty("image-path")
  var imgPath: String? = "composition.png"
  
  @JsonProperty("width")
  var width: Double = CompositionGraphWriter.defaultWidth
  
  @JsonProperty("height")
  var height: Double = CompositionGraphWriter.defaultHeight
}
