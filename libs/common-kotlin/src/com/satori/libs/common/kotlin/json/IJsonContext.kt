package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.databind.*

interface IJsonContext {
  val mapper: ObjectMapper
  
}