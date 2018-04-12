package com.satori.libs.common.kotlin.json

import com.fasterxml.jackson.annotation.*
import org.junit.*

class JsonLibTests : Assert() {
  data class TestJsonDataClass(
    @field:JsonProperty("my-property")
    var myProp: String? = null
  )
  
  enum class TestJsonEnum(private val value: String) {
    @JsonProperty("idle")
    IDLE("idle"),
    @JsonProperty("running")
    RUNNING("running"),
    @JsonProperty("completed")
    COMPLETED("completed")
  }
  
  @Test
  fun jsonDataClassTest() {
    val o = TestJsonDataClass("test")
    assertEquals(
      jsonParseAsTree("{'my-property':'test'}"),
      jsonValueToTree(o)
    )
  }
  
  @Test
  fun jsonEnumTest() {
    assertEquals(
      jsonParseAsTree("'running'"),
      jsonValueToTree(TestJsonEnum.RUNNING)
    )
    assertEquals(
      TestJsonEnum.COMPLETED,
      jsonParse<TestJsonEnum>("'completed'")
    )
  }
  
  @Test
  fun jsonParseTest() {
    
    val json = jsonObject {
      field("array", jsonArray {
        item(1)
        item("2")
      })
      field("test", "bla-bla")
    }
    assertEquals(json, jsonParseAsTree(json.toString()))
  }
  
  @Test
  fun jsonObjectTest() {
    assertEquals(
      jsonParseAsTree("{array:[1,'2'], test:'bla-bla'}"),
      jsonObject {
        field("array", jsonArray {
          item(1)
          item("2")
        })
        field("test", "bla-bla")
      }
    )
  }
  
  @Test
  fun jsonWriterTest() {
    assertEquals("""{"s":"foo","n":0,"a":[1,true,"bar"],"o":{"f":5.0},"b":true}""",
      jsonObjectToString {
        field("s", "foo")
        field("n", 0)
        field("a", jsonArray {
          item(1)
          item(true)
          item("bar")
          //field("a","a")
        })
        field("o", jsonObject {
          field("f", 5.0)
        })
        field("b", true)
      }
    )
  }
}
