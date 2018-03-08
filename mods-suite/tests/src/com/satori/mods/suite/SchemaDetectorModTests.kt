package com.satori.mods.suite

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.jsonSchema.*
import com.satori.libs.async.api.*
import com.satori.libs.async.core.*
import com.satori.libs.testlib.*
import com.satori.libs.testlib.json.*
import io.vertx.ext.unit.*
import io.vertx.ext.unit.junit.*
import kotlinx.coroutines.experimental.*
import org.junit.*
import org.junit.runner.*
import org.slf4j.*
import kotlin.coroutines.experimental.intrinsics.*

@RunWith(VertxUnitRunner::class)
class SchemaDetectorModTests : ModTest() {

  class DelayMod() : Mod() {
    val log = LoggerFactory.getLogger("delay-mod@${Integer.toHexString(System.identityHashCode(this))}")
    override fun onInput(inputName: String, data: JsonNode, cont: IAsyncHandler<*>) {
      onInput(inputName, data).onCompleted { ar ->
        if (ar.isSucceeded) cont.succeed() else cont.fail(ar.error)
      }
    }

    override fun onInput(inputName: String, data: JsonNode) = future(VertxFutureScope(context().vertx(), log)) {
      log.info("input: $data")
      yield(data).await()
      delay(data.asInt())
    }
  }

  @Test
  fun `order test`(context: TestContext) = asyncTest(context) {
    val mod = Composition().apply {
      val delay1 = addMod("delay1", DelayMod())
      val delay2 = addMod("delay2", DelayMod())
      linkModInput(delay1, "default", "default")
      linkModInput(delay2, "default", "default")
      linkOutput("delay1")
      linkOutput("delay2")
    }

    mod.init(this@SchemaDetectorModTests)

    val afj = AsyncForkJoin()
    afj.fork {
      mod.onInput("default", jsonNode(1000)).await()
    }
    afj.fork {
      suspendCoroutineOrReturn { cont ->
        mod.onInput("default", jsonNode(100)){ar->
          if(ar.isSucceeded) cont.resume(Unit) else cont.resumeWithException(ar.error)
        }
        return@suspendCoroutineOrReturn COROUTINE_SUSPENDED
      }
    }
    afj.fork {
      mod.onInput("default", jsonNode(10)).await()
    }

    afj.join().await()

    stopMod(mod)

    assertEquals(
      listOf(
        jsonNode(1000), jsonNode(1000),
        jsonNode(100), jsonNode(100),
        jsonNode(10), jsonNode(10)
      ),
      outQueue.toList()
    )

  }

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
