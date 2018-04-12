import com.sun.org.apache.bcel.internal.generic.*

pckg = config['pckg']
prefix = config['prefix'] ?: ""

typeMap = [:]

data.definitions.each { k, v ->
  buildTypeMap("${prefix}${k}", k, v, "#/definitions/${k}")
}

PACKAGE(pckg) {
  CLASS("GtfsProtoBufConverter") {
    processDefinitions(data.definitions)
  }
}

def processDefinitions(definitions, ref="#") {
  definitions.each { tname, tdef ->
    def tref = "${ref}/definitions/${tname}"
    if (tdef.properties) {
      processClass(tname, tdef, tref)
    } else if (tdef.enum) {
      if (tdef.enum.size() > 1) {
        processEnum(tname, tdef, tref)
      } else if (tdef.enum.size() == 1) {
        //buildConst(tname, tdef, tref)
      }
    } else if (tdef.oneOf) {
      processEnum(tname, tdef, tref)
    }
  }
}

def processClass(cname, cdef, cref) {
  def c = typeMap[cref]
  def fullName = c.name
  def fullId = c.id
  while (c.parent){
    c = c.parent;
    fullName = "${c.name}.${fullName}"
    fullId = "${c.id}.${fullId}"
  }
  METHOD("convert") {
    STATIC()
    def rtype = "${pckg}.${fullName}"
    TYPE(rtype)
    PARAM("value", "com.satori.libs.gtfs.GtfsRealtime.${fullId}")
    CODE("if(value == null){")
    CODE("  return null;")
    CODE("}")
    def result=
    DECL("result", rtype){
      NEW(rtype)
    }
    cdef.properties.each{pname, pdef->


      if(pdef.items){
        CODE("do{");
        def edef = pdef.items
        def p = typeMap[edef.$ref]
        def pfullName = p.name
        def pfullId = p.id
        while (p.parent){
          p = p.parent;
          pfullName = "${p.name}.${pfullName}"
          pfullId = "${p.id}.${pfullId}"
        }

        def ptype = "${pckg}.${pfullName}"
        CODE("  ${ptype}[] array = new ${ptype}[value.get${toPascal(pname)}Count()];");
        CODE("  for(int i=0; i<array.length; i+=1){");
        if (edef.type) {
          CODE("    array[i] = value.get${toPascal(pname)}(i);");
        } else if (edef.$ref){
          CODE("    array[i] = convert(value.get${toPascal(pname)}(i));");
        }
        CODE("  }");
        CODE("  result.${toCamel(pname)} = array;");
        CODE("}while(false);");
      }else{
        CODE("if(value.has${toPascal(pname)}()){");
        if (pdef.type) {
          CODE("  result.${toCamel(pname)} = value.get${toPascal(pname)}();");
        } else if (pdef.$ref){
          CODE("  result.${toCamel(pname)} = convert(value.get${toPascal(pname)}());");
        }
        CODE("}");
      }
    }
    /*

      return null;
    }
    GtfsPosition result = new GtfsPosition();

    if(val.hasLatitude()) {
      result.latitude = val.getLatitude();
    }
    if(val.hasLongitude()) result.longitude = val.getLongitude();
    if(val.hasBearing()) result.bearing = val.getBearing();
    if(val.hasOdometer()) result.odometer = val.getOdometer();
    if(val.hasSpeed()) result.speed = val.getSpeed();

    return result;
     */



    RETURN(result)
  }
  processDefinitions(cdef.definitions, cref)
}

def buildProp(pname, pdef) {

}


def processEnum(ename, edef, eref) {
  def c = typeMap[eref]
  def fullName = c.name
  def fullId = c.id
  while (c.parent){
    c = c.parent;
    fullName = "${c.name}.${fullName}"
    fullId = "${c.id}.${fullId}"
  }
  METHOD("convert") {
    STATIC()
    def rtype = "${pckg}.${fullName}"
    TYPE(rtype)
    PARAM("value", "com.satori.libs.gtfs.GtfsRealtime.${fullId}")

    CODE("if(value == null){")
    CODE("  return null;")
    CODE("}")
    RETURN() {
      EXP("${rtype}.fromInt(value.getNumber())")
    }
  }
}

def buildTypeMap(tname, aname, tdef, ref, parent=null){
  def type = tname
  def x = tdef.items
  while (x){
    type = "${type}[]"
    x = x.items
  }
  def context = [
    name: tdef.items ? null : toPascal(tname),
    id: tdef.items ? null : aname,
    type:type,
    parent:parent,
    tdef:tdef
  ]
  typeMap[ref] = context
  if(tdef.definitions){
    tdef.definitions.each{ k, v->
      buildTypeMap(k, k, v, "${ref}/definitions/${k}", context)
    }
  }
  if(tdef.items){
    buildTypeMap(tname, aname, tdef.items, "${ref}/items", context)
  }
  if(tdef.properties){
    tdef.properties.each{ k, v->
      buildTypeMap(k, k, v, "${ref}/properties/${k}", context)
    }
  }
}
