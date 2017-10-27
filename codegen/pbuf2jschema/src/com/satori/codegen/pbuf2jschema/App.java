package com.satori.codegen.pbuf2jschema;

import com.satori.codegen.pbuf2jschema.parser.*;
import com.satori.codegen.pbuf2jschema.schema.*;

import java.io.*;
import java.nio.file.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.*;
import com.fasterxml.jackson.module.afterburner.*;

public class App {
  
  public static final ObjectMapper mapper = new ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());
  
  public static void run(InputStream schemaStream, OutputStream outputStream) throws Exception {
    ProtoBufParser s = new ProtoBufParser(schemaStream);
    Unit res = //FlattenProcessor.process(
      SchemaPostProcessor.process(s.parse());
    //);
    mapper.writerWithDefaultPrettyPrinter().writeValue(
      outputStream, res
    );
  }
  
  public static void run(Path schemaPath, Path outPath) throws Exception {
    try (InputStream schemaStream = Files.newInputStream(schemaPath)) {
      outPath.getParent().toFile().mkdirs();
      OpenOption[] options = new OpenOption[]{
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
      };
      try (OutputStream outStream = Files.newOutputStream(outPath, options)) {
        run(schemaStream, outStream);
      }
    }
  }
  
  public static void main(String... args) throws Exception {
    System.out.println("executing protobuf parser v0.0 with command line arguments:");
    String schema = null;
    String out = null;
    for (int i = 0; i < args.length - 1; i += 2) {
      String v = args[i + 1];
      System.out.println(args[i] + " " + v);
      switch (args[i]) {
        case "-schema": {
          schema = v;
          break;
        }
        case "-out": {
          out = v;
          break;
        }
      }
    }
    run(Paths.get(schema), Paths.get(out));
  }
}
