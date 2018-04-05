package com.satori.libs.composition.drawer

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppArgs {
  @JsonProperty("cfg-path")
  var cfgPath: String? = null
  
  @JsonProperty("img-path")
  var imgPath: String = "composition.png"
  
  @JsonProperty("img-format")
  var imgFormat: String = defaultImgFormat
  
  @JsonProperty("block-width")
  var blockWidth: Double = CompositionGraphWriter.defaultWidth
  
  @JsonProperty("block-height")
  var blockHeight: Double = CompositionGraphWriter.defaultHeight
  
  companion object {
    val defaultImgFormat = "PNG"
  }
}
