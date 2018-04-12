package com.satori.libs.common.kotlin

import java.time.*
import java.time.format.*

fun instantFromRfc1123(rfc1123: String): Instant {
  val temporal = DateTimeFormatter.RFC_1123_DATE_TIME.parse(rfc1123)
  return Instant.from(temporal)
}
