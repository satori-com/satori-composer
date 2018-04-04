package com.satori.libs.composition.drawer

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppArgs {
  @JsonProperty("cfg-path")
  var cfgPath: String? = null
  
  @JsonProperty("image-path")
  var imgPath: String? = "composition.png"
  
  @JsonProperty("block-width")
  var blockWidth: Double = CompositionGraphWriter.defaultWidth
  
  @JsonProperty("block-height")
  var blockHeight: Double = CompositionGraphWriter.defaultHeight
}
