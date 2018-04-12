import com.sun.org.apache.bcel.internal.generic.*

pckg = config['pckg']
prefix = config['prefix'] ?: ""

typeMap = [:]

data.definitions.each { k, v ->
  buildTypeMap("${prefix}${k}",v, "#/definitions/${k}")
}

PACKAGE(pckg) {
  processDefinitions(data.definitions)
}

def processDefinitions(definitions, ref="#") {
  definitions.each { tname, tdef ->
    def tref = "${ref}/definitions/${tname}"
    if (tdef.properties) {
      buildClass(tname, tdef, tref)
    } else if (tdef.enum) {
      if (tdef.enum.size() > 1) {
        buildEnum(tname, tdef, tref)
      } else if (tdef.enum.size() == 1) {
        buildConst(tname, tdef, tref)
      }
    } else if (tdef.oneOf) {
      buildUnion(tname, tdef, tref)
    }
  }
}

def buildClass(cname, cdef, cref) {
  def c = typeMap[cref]
  CLASS(c.name) {
    EXTENDS("com.satori.libs.gtfs.GtfsObject")
    ANNOTATION("com.fasterxml.jackson.annotation.JsonInclude"){
      FIELD(TREF("com.fasterxml.jackson.annotation.JsonInclude.Include"), "NON_NULL")
    }
    def classType = builder.$context
    COMMENT(cdef.description);
    def properties = []
    cdef.properties.each { pname, pdef ->
      properties <<
      buildProperty(pname, pdef, "${cref}/properties/${pname}")
    }

    METHOD("equals") {
      ANNOTATION(Override)
      TYPE(boolean)
      def o =
      PARAM("o", Object)

      IF($EQ(THIS, o)) {
        THEN {
          RETURN(TRUE)
        }
      }

      IF($OR($EQ(o, NULL), $EXP("getClass() != o.getClass()"))) {
        THEN {
          RETURN(FALSE)
        }
      }

      def that =
      DECL("that", classType) { CAST(o, classType) }

      CODE("if (!super.equals(o)) {")
      CODE("  return false;")
      CODE("}")

      properties.each { p ->
        if (p.type().isArray()) {
          IF($NOT($CALL(Arrays, "equals") { FIELD(THIS, p); FIELD(that, p) })) {
            RETURN(FALSE)
          }
        } else if (p.type().isPrimitive()) {
          IF($NOT($EQ { FIELD(THIS, p); FIELD(that, p) })) {
            RETURN(FALSE)
          }
        } else {
          IF($NOT($CALL(Objects, "equals") { FIELD(THIS, p); FIELD(that, p) })) {
            RETURN(FALSE)
          }
        }
      }

      RETURN(TRUE)
    }

    METHOD("hashCode") {
      TYPE(int)
      ANNOTATION(Override)
      def result =
      DECL("result", int){EXP("super.hashCode()")}

      properties.each { p ->
        ASSIGN(result) {
          SUM {
            EXP(result.mul($LIT(31)))
            if (p.type().isArray()) {
              CALL(Arrays, "hashCode") { FIELD(THIS, p) }
            } else if (p.type().isPrimitive()) {
              CALL(FIELD(THIS, p), "hashCode")
            } else {
              EXP("this.${p.name()} != null ? this.${p.name()}.hashCode() : 0")
            }
          }
        }
      }
      RETURN(result)
    }
    processDefinitions(cdef.definitions, cref)
  }
}

def buildUnion(uname, udef, uref) {
  def u = typeMap[uref]
  ENUM(u.name) {
    def enumType = builder.$context
    COMMENT(udef.description)
    def type = null
    def options = []
    udef.oneOf.each { v ->
      def o = typeMap[v.$ref]
      options <<
      OPTION(o.id) {
        COMMENT(o.tdef.description)
        def resolvedType = toNonNullable(resolveType(o.name, o.tdef))
        type = (!type || type == resolvedType) ? resolvedType : Object.class.name
        ARG(o.tdef.enum[0])
      }
    }

    def valueField =
      FIELD("value") {
        TYPE(type)
        ANNOTATION("com.fasterxml.jackson.annotation.JsonValue")
      }

    CTOR() {
      def valueParam =
        PARAM("value", type)

      ASSIGN($FIELD(THIS, valueField), valueParam)
      //CODE("this.value = value;")
    }

    METHOD("from${type.capitalize()}") {
      STATIC()
      TYPE(enumType)
      ANNOTATION("com.fasterxml.jackson.annotation.JsonCreator")
      def value = PARAM("value", type)
      SWITCH(value) {
        options.each { o ->
          CASE(o.args[0]) {
            RETURN(o)
          }
        }
        DEFAULT {
          RETURN(NULL)
        }
      }
    }
    processDefinitions(udef.definitions, uref)
  }
}

def buildConst(cname, cdef, cref) {
}

def buildProperty(pname, pdef, pref) {
  FIELD(toCamel(pname)) {
    COMMENT(pdef.description)
    TYPE(resolveType(pname, pdef))
    ANNOTATION("com.fasterxml.jackson.annotation.JsonProperty") {
      PARAM(pname)
    }
  }
}

def buildTypeMap(tname, tdef, ref, parent=null){
  def type = tname
  def x = tdef.items
  while (x){
    type = "${type}[]"
    x = x.items
  }
  def context = [
    name: tdef.items ? null : toPascal(tname),
    id: tdef.items ? null : tname,
    type:type,
    parent:parent,
    tdef:tdef
  ]
  typeMap[ref] = context
  if(tdef.definitions){
    tdef.definitions.each{ k, v->
      buildTypeMap(k, v, "${ref}/definitions/${k}", context)
    }
  }
  if(tdef.items){
    buildTypeMap(tname, tdef.items, "${ref}/items", context)
  }
  if(tdef.properties){
    tdef.properties.each{ k, v->
      buildTypeMap(k, v, "${ref}/properties/${k}", context)
    }
  }
}

def getTypeName(String name){
  return toPascal(name)
}

def resolveType(pname, pdef) {
  if (pdef.$ref) {
    return typeMap[pdef.$ref].type ?: Object.class.name
  } else if(!pdef.type || pdef.type == "object"){
    return getTypeName(pname)
  } else if (pdef.type == "array") {
    return "${resolveType(pname, pdef.items)}[]"
  }
  return [
    "string:"      : String.class.name,
    "number:"      : Double.class.name,
    "number:float" : Float.class.name,
    "number:double": Double.class.name,
    "boolean:"     : Boolean.class.name,
    "integer:"     : Integer.class.name,
    "integer:int32": Integer.class.name,
    "integer:int64": Long.class.name,
    "any:"         : "com.fasterxml.jackson.annotation.JsonNode"
  ]["${pdef.type}:${pdef.format ?: ''}"] ?: Object.class.name
}

def toNonNullable(String type) {
  return [
    (Double.class.name) : double.class.name,
    (Float.class.name)  : float.class.name,
    (Integer.class.name): int.class.name,
    (Long.class.name)   : long.class.name,
    (Boolean.class.name): boolean.class.name
  ][type] ?: type
}