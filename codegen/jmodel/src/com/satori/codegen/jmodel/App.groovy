package com.satori.codegen.jmodel

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jdk8.*
import com.fasterxml.jackson.module.afterburner.*
import com.satori.codegen.jmodel.codemodel.*
import com.satori.codegen.groovy.*
import com.sun.codemodel.*
import org.codehaus.groovy.control.*

import java.nio.file.*

class App {
  static final ObjectMapper mapper = new ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .registerModule(new AfterburnerModule())
    .registerModule(new Jdk8Module());

  static JsonNode loadAsJson(String resource) throws Exception {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    classLoader.getResourceAsStream(resource).withCloseable { stream ->
      return mapper.readTree(stream);
    }
  }

  static String loadAsString(String resource) throws Exception {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    classLoader.getResourceAsStream(resource).withCloseable { stream ->
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = stream.read(buffer)) != -1) {
        byteStream.write(buffer, 0, length);
      }
      return byteStream.toString();
    }
  }


  static void main(String... args) {

    println "running jmodel code generation v0.0 ..."
    println "java version: ${System.getProperty('java.version')}"
    println "groovy version: ${GroovySystem.version}"
    println "command line arguments:"

    Path dataPath = null;
    Path templatePath = null;
    Path outPath = null;

    final Binding binding = new Binding()
    def config = [:]
    binding.config = config

    for (int i = 0; i < args.length - 1; i += 2) {
      String val = args[i + 1]
      String opt = args[i]
      if (!opt.startsWith("-")) {
        throw new Exception("invalid parameter: " + opt);
      }
      println "${opt} ${val}"
      config[opt.substring("-".length())] = val
      switch (opt) {
        case "-data":
          dataPath = Paths.get(val)
          break
        case "-template":
          templatePath = Paths.get(val)
          break
        case "-out":
          outPath = Paths.get(val)
          break
      }
    }

    if (templatePath == null) {
      throw new Exception("missing required argument: 'template'")
    }

    JModelScript script = Files.newBufferedReader(templatePath).withCloseable { reader ->
      CompilerConfiguration compConfig = new CompilerConfiguration()
      /*compConfig.addCompilationCustomizers(new SetClassName(
        JModelScript.toPascal(templatePath.fileName.toString())
      ))*/
      compConfig.setScriptBaseClass(JModelScript.class.getName())
      GroovyShell sh = new GroovyShell(compConfig)
      return (JModelScript) sh.parse(reader, templatePath.toString())
    }

    if (dataPath != null) {
      def data = Files.newBufferedReader(dataPath).withCloseable { reader ->
        JacksonToGroovy.convert(mapper.readTree(reader))
      }
      binding.data = data
    }
    JType t;

    String comments = "";
    comments.eachLine { l ->
      if (l != null && !l.isEmpty()) {
        COMMENT(l)
      }
    }

    script.setBinding(binding);
    script.run();
    outPath.toFile().mkdirs();
    script.getModel().build(outPath.toFile());
  }

}
