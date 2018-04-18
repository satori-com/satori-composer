package com.satori.mods.suite

import com.fasterxml.jackson.module.jsonSchema.*
import com.satori.libs.common.kotlin.json.*
import com.satori.libs.testlib.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import org.junit.*
import org.junit.runner.*

@RunWith(VertxUnitRunner::class)
class SchemaDetectorModTests : ModTest(), IJsonContext by DefaultJsonContext {
  
  @Test
  fun `basic test`(context: TestContext) = asyncTest(context) {
    val mod = SchemaDetectorMod()
    startMod(mod)
    
    mod.onInput("default", jsonParseAsTree("""{
      "source_agency": "MZ",
      "dranik": {
        "app": {
          "name": "mzshuttle",
          "pkg": "com.mz.android.driver",
          "ver": "1.1.20"
        },
        "power": {}
      },
      "f": "mob-rt",
      "label": "Page Mill",
      "vehicle_state": "active",
      "id": "355022075462007",
      "position": {
        "accuracy": 6,
        "bearing": 214,
        "lat": 37.422127,
        "lng": -122.14282,
        "speed": 12.462797
      },
      "service": {
        "in_service": true
      },
      "transportation": {
        "trip_state": "in_service",
        "route": {
          "ref_id": "PAGE_MILL"
        }
      },
      "timestamp": 1516945647536,
      "type": "shuttle"
    }""")).await()
    
    val res = outQueue.pollFirst()
    
    jsonTreeToValue<JsonSchema>(res)
    
    assertEquals(jsonParseAsTree("""{
      "type": "object",
      "properties": {
        "source_agency": {
          "type": "string"
        },
        "dranik": {
          "type": "object",
          "properties": {
            "app": {
              "type": "object",
              "properties": {
                "name": {"type": "string"},
                "pkg": {"type": "string"},
                "ver": {"type": "string"}
              }
            }
          }
        },
        "f": {"type": "string"},
        "label": {"type": "string"},
        "vehicle_state": {"type": "string"},
        "id": {"type": "string"},
        "position": {
          "type": "object",
          "properties": {
            "accuracy": {"type": "integer"},
            "bearing": {"type": "integer"},
            "lat": {"type": "number"},
            "lng": {"type": "number"},
            "speed": {"type": "number"}
          }
        },
        "service": {
          "type": "object",
          "properties": {
            "in_service": {"type": "boolean"}
          }
        },
        "transportation": {
          "type": "object",
          "properties": {
            "trip_state": {"type": "string"},
            "route": {
              "type": "object",
              "properties": {
                "ref_id": {"type": "string"}
              }
            }
          }
        },
        "timestamp": {"type": "integer"},
        "type": {"type": "string"}
      }
    }"""), res)
    stopMod(mod)
  }
}
