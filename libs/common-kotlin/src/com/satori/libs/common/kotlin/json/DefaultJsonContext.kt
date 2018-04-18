package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.afterburner.*

object DefaultJsonContext: IJsonContext{
  override var mapper = ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
    .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(AfterburnerModule())
}