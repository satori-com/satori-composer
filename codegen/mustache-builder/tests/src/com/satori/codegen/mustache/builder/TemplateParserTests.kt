package com.satori.codegen.mustache.builder

import com.github.javaparser.*
import com.github.javaparser.ast.body.*
import org.junit.*
import java.io.*

class TemplateParserTests: Assert() {
  
  @Test
  fun `generated fields should be in camel case`(){
    val code = """
      {{PascalCase}}
      {{kebab-case}}
      {{camelCase}}
      {{snake_case}}
    """.trimIndent()
    
    val template= TemplateParser.parse(code, "test")
  
    template.ops.apply {
      assertNotNull(find { it is OpNode.WriteVarValue && it.varName == "pascalCase" })
      assertNotNull(find { it is OpNode.WriteVarValue && it.varName == "kebabCase" })
      assertNotNull(find { it is OpNode.WriteVarValue && it.varName == "camelCase" })
      assertNotNull(find { it is OpNode.WriteVarValue && it.varName == "snakeCase" })
    }
    template.model.apply {
      assertEquals( "Test", className)
      assertEquals( "test", name)
  
      assertEquals( "PascalCase", children["PascalCase"]!!.name)
      assertEquals( "pascalCase", children["PascalCase"]!!.varName)
      assertEquals( "PascalCase", children["PascalCase"]!!.className)
  
      assertEquals( "kebab-case", children["kebab-case"]!!.name)
      assertEquals( "kebabCase", children["kebab-case"]!!.varName)
      assertEquals( "KebabCase", children["kebab-case"]!!.className)
  
      assertEquals( "camelCase", children["camelCase"]!!.name)
      assertEquals( "camelCase", children["camelCase"]!!.varName)
      assertEquals( "CamelCase", children["camelCase"]!!.className)
  
      assertEquals( "snake_case", children["snake_case"]!!.name)
      assertEquals( "snakeCase", children["snake_case"]!!.varName)
      assertEquals( "SnakeCase", children["snake_case"]!!.className)
    }
  
    val modelOutputStream = ByteArrayOutputStream()
    ModelsCodeGen.generate("com.satori.test", template.model, modelOutputStream )
  
    val modelClassCode = modelOutputStream.toString()
    val modelClass = JavaParser.parse(modelClassCode)
    (modelClass.types[0] as ClassOrInterfaceDeclaration).apply {
      assertNotNull(members.find { it is FieldDeclaration && it.toString() == "public String camelCase;" })
      assertNotNull(members.find { it is FieldDeclaration && it.toString() == "public String pascalCase;" })
      assertNotNull(members.find { it is FieldDeclaration && it.toString() == "public String kebabCase;" })
      assertNotNull(members.find { it is FieldDeclaration && it.toString() == "public String snakeCase;" })
    }
  
    val rendererOutputStream = ByteArrayOutputStream()
    RendererCodeGen.generate("com.satori.test", "TestRenderer", template, rendererOutputStream )
  
    val rendererClassCode = rendererOutputStream.toString()
    val rendererClass = JavaParser.parse(rendererClassCode)
    
    
  }
}