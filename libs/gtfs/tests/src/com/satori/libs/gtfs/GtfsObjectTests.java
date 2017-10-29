package com.satori.libs.gtfs;

import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.*;

public class GtfsObjectTests extends Assert {
  public static final ObjectMapper mapper = new ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
    .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
  
  @Test
  public void serializationTest() throws Exception {
    String json = "{int:10,bool:true,double:120.0, string:'abc', null:null, object:{x:0, y:0}, array:[1,2,3]}";
    
    GtfsObject o = new GtfsObject();
    o.ext("int", 10);
    o.ext("bool", true);
    o.ext("double", 120.0);
    o.ext("string", "abc");
    o.ext("null", NullNode.instance);
    o.ext("object", mapper.readTree("{x:0, y:0}"));
    o.ext("array", mapper.readTree("[1,2,3]"));
    
    assertEquals(
      mapper.readTree(json),
      mapper.readTree(mapper.writeValueAsString(o))
    );
  
    assertEquals(
      o, mapper.readValue(json, GtfsObject.class)
    );
  
    assertEquals(
      new GtfsObject(), mapper.readValue("{}", GtfsObject.class)
    );
  }
  
  @Test
  public void primitiveTypesTest(){
    
    GtfsObject o = new GtfsObject();
    assertTrue(o.ext().isEmpty());
    
    assertNull(o.ext("foo"));
  
    o.ext("foo", 10);
    assertEquals(IntNode.valueOf(10), o.ext("foo"));
  
    o.ext("foo", true);
    assertEquals(BooleanNode.TRUE, o.ext("foo"));
  
    o.ext("foo", 12L);
    assertEquals(LongNode.valueOf(12), o.ext("foo"));
  
    o.ext("foo", 120.0);
    assertEquals(DoubleNode.valueOf(120), o.ext("foo"));
  
    o.ext("foo", 120.0f);
    assertEquals(FloatNode.valueOf(120), o.ext("foo"));
  }
  
  @Test
  public void nullableTypesTest(){
    
    GtfsObject o = new GtfsObject();
    assertTrue(o.ext().isEmpty());
    
    assertNull(o.ext("foo"));
    
    o.ext("foo", new Integer( 10));
    assertEquals(IntNode.valueOf(10), o.ext("foo"));
    o.ext("foo", (Integer) null);
    assertTrue(o.ext("foo") == null && o.ext().isEmpty());
  
    o.ext("foo", new Long(12L));
    assertEquals(LongNode.valueOf(12), o.ext("foo"));
    o.ext("foo", (Long) null);
    assertTrue(o.ext("foo") == null && o.ext().isEmpty());
  
    o.ext("foo", (Boolean) true);
    assertEquals(BooleanNode.TRUE, o.ext("foo"));
    o.ext("foo", (Boolean) null);
    assertTrue(o.ext("foo") == null && o.ext().isEmpty());
    
    
    o.ext("foo", new Double(120.0));
    assertEquals(DoubleNode.valueOf(120), o.ext("foo"));
    o.ext("foo", (Double) null);
    assertTrue(o.ext("foo") == null && o.ext().isEmpty());
    
    o.ext("foo", new Float(120.0f));
    assertEquals(FloatNode.valueOf(120), o.ext("foo"));
    o.ext("foo", (Float) null);
    assertTrue(o.ext("foo") == null && o.ext().isEmpty());
  
  
    o.ext("foo", "bar");
    assertEquals(TextNode.valueOf("bar"), o.ext("foo"));
    o.ext("foo", (String) null);
    assertTrue(o.ext("bar") == null && o.ext().isEmpty());

    o.ext("foo", NullNode.instance);
    assertEquals(NullNode.instance, o.ext("foo"));
    
    o.ext("foo", (JsonNode) null);
    assertNull(o.ext("foo"));
    assertTrue(o.ext().isEmpty());
    
    //assertNull(o.ext());
  }
  
  @Test
  public void equalityTest(){
    
    GtfsObject o1 = new GtfsObject();
    GtfsObject o2 = new GtfsObject();
  
    assertEquals(o1, o2);
    assertEquals(o1, o1);
    assertNotEquals(o1, new MyGtfsObject());
    assertEquals(new MyGtfsObject(), new MyGtfsObject());
    
    
    o1.ext("foo", 10);
    assertNotEquals(o1, o2);
    o2.ext("foo", 10);
  
    assertEquals(o1, o2);
  
    o1.ext("boo", true);
    assertNotEquals(o1, o2);
    o2.ext("boo", false);
    assertNotEquals(o1, o2);
    o2.ext("boo", true);
  
    assertEquals(o1, o2);
  }
  
  private class MyGtfsObject extends GtfsObject{
  }
}