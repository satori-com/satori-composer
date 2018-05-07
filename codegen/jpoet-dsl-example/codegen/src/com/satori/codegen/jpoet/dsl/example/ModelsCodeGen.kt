package com.satori.codegen.jpoet.dsl.example

import com.fasterxml.jackson.annotation.*
import com.satori.codegen.jpoet.dsl.*
import com.satori.codegen.jpoet.dsl.extensions.*
import com.satori.codegen.jpoet.dsl.scopes.*
import com.satori.codegen.utils.*
import com.squareup.javapoet.*
import com.squareup.javapoet.TypeName
import graphql.Scalars.*
import graphql.language.*
import graphql.schema.*
import graphql.schema.idl.*
import groovy.lang.*
import groovy.text.*
import java.io.*
import java.math.*
import java.nio.file.*

class ModelsCodeGen(val cfg: ModelsCodeGenConfig) {
  val schema: TypeDefinitionRegistry
  val ignore = HashSet<String>()
  var query: OperationTypeDefinition? = null
  var fileHeader = """
    auto generated
    don't modify
  """.trimIndent()
  
  init {
    println("reading schema '${cfg.schemaPath}'...")
    schema = Files.newBufferedReader(cfg.schemaPath).use {
      SchemaParser().parse(it)
    }
    schema.schemaDefinition().get().operationTypeDefinitions.forEach {
      ignore.add((it.type as graphql.language.TypeName).name)
      if (it.name == "query") {
        if (query != null) {
          throw Exception("multiple query operation")
        }
        query = it
      }
    }
  }
  
  fun path(pckg: String, fileName: String): Path {
    val pckgDir = pckg.replace(".", "/")
    return Paths.get("${cfg.outDir}/$pckgDir/$fileName")
  }
  
  val typeMap = HashMap<String, TypeName>()
  
  fun generateCode() {
    val pckg = "${cfg.pckg}.models"
    
    for ((tname, tdef) in schema.types()) {
      if (ignore.contains(tname)) continue
      val className = "${cfg.classPrefix}${tname.pascalCase()}"
      val filePath = path(pckg, "$className.java")
      
      typeMap[tname] = ClassName.get(pckg, className)
      
      FILE(filePath) {
        COMMENT(fileHeader)
        PACKAGE(pckg) {
          when (tdef) {
            is ObjectTypeDefinition -> generateClass(tdef)
            is EnumTypeDefinition -> generateEnum(tdef)
            is InterfaceTypeDefinition -> generateInterface(tdef)
            else -> throw Exception("unsupported type '${tdef.javaClass.simpleName}'")
          }
        }
      }
      println("file generated '${filePath.fileName}' ('${filePath}')")
    }
    
    query?.let {
      generateQueryFetcher(schema.getType(it.type).get() as ObjectTypeDefinition)
    }
    generateSchema()
    System.out.flush()
  }
  
  fun generateQueryFetcher(tdef: ObjectTypeDefinition) {
    
    val pckg = "${cfg.pckg}.apis"
    
    val className = "I${cfg.classPrefix}${tdef.name.pascalCase()}Fetcher"
    val filePath = path(pckg, "$className.java")
    
    FILE(filePath) {
      COMMENT(fileHeader)
      PACKAGE(pckg) {
        INTERFACE(className) {
          for (fdef in tdef.children) {
            if (fdef !is FieldDefinition) continue
            val ftype = resolveType(fdef.type)
            
            FUN(fdef.name) {
              RETURNS(ftype)
              ABSTRACT()
              for (idef in fdef.inputValueDefinitions) {
                val itype = resolveType(idef.type)
                ARG(idef.name, itype)
              }
            }
            
          }
        }
      }
    }
    println("file generated '${filePath.fileName}' ('${filePath}')")
  }
  
  fun generateSchema() {
    
    val pckg = cfg.pckg
    
    val className = "${cfg.classPrefix}Schema"
    val filePath = path(pckg, "$className.java")
    
    FILE(System.out) {
      COMMENT(fileHeader)
      PACKAGE(pckg) {
        CLASS(className) {
          STATIC()
          
          
          for ((tname, tdef) in schema.types()) {
            if (ignore.contains(tname)) continue
            
            when (tdef) {
              is ObjectTypeDefinition -> generateSchemaField(tdef)
              is EnumTypeDefinition -> generateSchemaField(tdef)
              is InterfaceTypeDefinition -> generateSchemaField(tdef)
              else -> throw Exception("unsupported type '${tdef.javaClass.simpleName}'")
            }
            println("file generated '${filePath.fileName}' ('${filePath}')")
          }
          
        }
      }
    }
    println("file generated '${filePath.fileName}' ('${filePath}')")
  }
  
  fun List<String>.concat(): String {
    val sb = StringBuilder()
    forEach { sb.append(it) }
    return sb.toString()
  }
  
  fun ITypeScope.generateSchemaField(tdef: ObjectTypeDefinition) {
    //val ftype = resolveType(fdef)
    FIELD<GraphQLObjectType>(tdef.name) {
      STATIC()
      INIT("""
        `GraphQLObjectType.newObject()
        `  .name("${tdef.name}")
        `  ${tdef.fieldDefinitions.map { fdef ->
        """.field(newFieldDefinition()
        `    .name("${fdef.name}")
        `    .type()
        `  )
        `  """
      }.concat()}
        `  .build()
      """.trimMargin("`"))
    }
  }
  
  /*.name("Human")
  
  .field(newFieldDefinition()
    .name("id")
    .type(nonNull(GraphQLString))
  )
  
  .field(newFieldDefinition()
  .name("name")
  .description("The name of the human.")
  .type(GraphQLString))
  .field(newFieldDefinition()
  .name("friends")
  .description("The friends of the human, or an empty list if they have none.")
  .type(list(characterInterface))
  .dataFetcher(StarWarsData.getFriendsDataFetcher()))
  .field(newFieldDefinition()
  .name("appearsIn")
  .description("Which movies they appear in.")
  .type(list(episodeEnum)))
  .field(newFieldDefinition()
  .name("homePlanet")
  .description("The home planet of the human, or null if unknown.")
  .type(GraphQLString))
  .build();*/
  
  fun ITypeScope.generateSchemaField(tdef: EnumTypeDefinition) {
    FIELD<GraphQLEnumType>(tdef.name) {
      STATIC()
      INIT("""
        GraphQLEnumType.newEnum()
          .name("${tdef.name}")
          ${tdef.enumValueDefinitions.map { edef ->
        ".value(\"${edef.name}\")"
      }.joinToString("\n          ")}
          .build()
      """.trimIndent())
    }
  }
  
  fun groovyTemplate(text: String, scope: Any): String {
    StringWriter().use {
      val engine = GStringTemplateEngine()
      val writable = engine.createTemplate(text.replace('#', '$')).make()
      (writable as Closure<*>).delegate = scope
      writable.writeTo(it)
      return it.toString()
    }
  }
  
  fun ITypeScope.generateSchemaField(fdef: InterfaceTypeDefinition) {
    FIELD<GraphQLInterfaceType>(fdef.name) {
      STATIC()
      fdef.fieldDefinitions
      INIT(groovyTemplate("""
        GraphQLInterfaceType.newInterface()
          .name(${fdef.name})
          <% fieldDefinitions.forEach{%>
          .field(newFieldDefinition()
            .name("#{it.name}")
          )
          .build()
          <% }%>
      """.trimIndent(), fdef))
    }
  }
  
  /*
  
  public static GraphQLInterfaceType characterInterface = newInterface()
            .name("Character")
            .description("A character in the Star Wars Trilogy")
            .field(newFieldDefinition()
                    .name("id")
                    .description("The id of the character.")
                    .type(nonNull(GraphQLString)))
            .field(newFieldDefinition()
                    .name("name")
                    .description("The name of the character.")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("friends")
                    .description("The friends of the character, or an empty list if they have none.")
                    .type(list(typeRef("Character"))))
            .field(newFieldDefinition()
                    .name("appearsIn")
                    .description("Which movies they appear in.")
                    .type(list(episodeEnum)))
            .typeResolver(StarWarsData.getCharacterTypeResolver())
            .build();
   */
  
  fun IPackageScope.generateClass(tdef: ObjectTypeDefinition) {
    CLASS(tdef.name) {
      ANNOTATION<JsonInclude> {
        ARG(JsonInclude.Include.NON_DEFAULT)
      }
      ANNOTATION<JsonIgnoreProperties> {
        ARG("ignoreUnknown", true)
      }
      
      for (field in tdef.children) {
        if (field !is FieldDefinition) continue
        val ftype = resolveType(field.type) ?: continue
        
        FIELD(field.name, ftype) {
          ANNOTATION<JsonProperty>(field.name)
        }
      }
      
      tdef.implements.forEach {
        IMPLEMENTS(resolveType(it))
        
        val idef = schema.getType(it).get()
        
        for (field in idef.children) {
          if (field !is FieldDefinition) continue
          val ftype = resolveType(field.type) ?: continue
          
          GETTER(field.name) {
            ANNOTATION<Override>()
            RETURNS(ftype)
            CODE("""
              return ${fieldName(field.name)};
            """.trimIndent())
          }
          
        }
      }
    }
  }
  
  fun IPackageScope.generateInterface(tdef: InterfaceTypeDefinition) {
    INTERFACE(tdef.name) {
      ANNOTATION<JsonInclude> {
        ARG(JsonInclude.Include.NON_DEFAULT)
      }
      ANNOTATION<JsonIgnoreProperties> {
        ARG("ignoreUnknown", true)
      }
      
      for (field in tdef.children) {
        if (field !is FieldDefinition) continue
        val ftype = resolveType(field.type) ?: continue
        
        GETTER(field.name) {
          PUBLIC()
          ABSTRACT()
          RETURNS(ftype)
        }
        
      }
    }
  }
  
  fun IPackageScope.generateEnum(tdef: EnumTypeDefinition) {
    ENUM(tdef.name) {
      
      for (odef in tdef.enumValueDefinitions) {
        OPTION(odef.name) {
          ANNOTATION<JsonProperty>(odef.name)
          ARG(odef.name)
        }
      }
      
      FIELD<String>("value")
      CTOR {
        ARG<String>("value")
        CODE("""
          this.value = value;
        """.trimIndent())
      }
      
    }
  }
  
  class TypeMapping(val nullable: TypeName, val nonNullable: TypeName = nullable)
  
  val primitiveTypes = HashMap<String, TypeMapping>().apply {
    
    fun reg(name: String, nullable: TypeName, nonNullable: TypeName = nullable) {
      put(name, TypeMapping(nullable, nonNullable))
    }
    
    reg(GraphQLString.name, ClassName.get(String::class.java))
    reg(GraphQLID.name, ClassName.get(String::class.java))
    reg(GraphQLBoolean.name, ClassName.get(java.lang.Boolean::class.java), ClassName.BOOLEAN)
    reg(GraphQLByte.name, ClassName.get(java.lang.Byte::class.java), ClassName.BYTE)
    reg(GraphQLShort.name, ClassName.get(java.lang.Short::class.java), ClassName.SHORT)
    reg(GraphQLInt.name, ClassName.get(java.lang.Integer::class.java), ClassName.INT)
    reg(GraphQLLong.name, ClassName.get(java.lang.Long::class.java), ClassName.LONG)
    reg(GraphQLFloat.name, ClassName.get(java.lang.Float::class.java), ClassName.FLOAT)
    reg(GraphQLBigDecimal.name, ClassName.get(BigDecimal::class.java))
    reg(GraphQLBigInteger.name, ClassName.get(BigInteger::class.java))
  }
  
  fun resolveType(type: Type, nullable: Boolean = true): com.squareup.javapoet.TypeName {
    val pckgModels = "${cfg.pckg}.models" // TODO: fix it
    when (type) {
      is ListType -> {
        return ArrayTypeName.of(resolveType(type.type))
      }
      is NonNullType -> {
        return resolveType(type.type, false)
      }
      is graphql.language.TypeName -> {
        primitiveTypes[type.name]?.let {
          return if (nullable) it.nullable else it.nonNullable
        }
        return ClassName.get(pckgModels, type.name)
      }
      else -> throw Exception("unsupported type '${type.javaClass.simpleName}'")
    }
  }
  
}


