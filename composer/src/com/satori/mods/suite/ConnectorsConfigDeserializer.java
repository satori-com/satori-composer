package com.satori.mods.suite;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

public class ConnectorsConfigDeserializer extends JsonDeserializer<HashMap<String, ArrayList<String>>> {
  @Override
  public HashMap<String, ArrayList<String>> deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    ObjectCodec oc = jp.getCodec();
    if (jp.currentToken() == JsonToken.START_OBJECT) {
      return oc.readValue(jp, new TypeReference<HashMap<String, ArrayList<String>>>() {
      });
    }
    
    ArrayList<String> val = oc.readValue(jp, new TypeReference<ArrayList<String>>() {
    });
    if (val == null) {
      return null;
    }
    HashMap<String, ArrayList<String>> res = new HashMap<>();
    res.put("default", val);
    return res;
  }
}