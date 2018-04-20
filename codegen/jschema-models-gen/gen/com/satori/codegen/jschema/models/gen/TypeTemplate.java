
package com.satori.codegen.jschema.models.gen;

import java.io.Writer;


/**
 * auto generated
 * don't modify
 * 
 */
public class TypeTemplate {


    public static void render0(Writer _os, TypeModel.PropertiesItem _model, boolean _last)
        throws Exception
    {
        _os.write("  @JsonProperty(\"");
        _os.write(_model.schemaName);
        _os.write("\")\n  public ");
        _os.write(_model.type);
        _os.write(" ");
        _os.write(_model.varName);
        _os.write(";\n");
    }

    public static void render1(Writer _os, TypeModel.TypesItem _model, boolean _last)
        throws Exception
    {
        _os.write("  ");
        TypeTemplate.render(_os, _model.type);
        _os.write("\n");
    }

    public static void render(Writer _os, TypeModel _model)
        throws Exception
    {
        _os.write("@JsonInclude(JsonInclude.Include.NON_DEFAULT)\n@JsonIgnoreProperties(ignoreUnknown = true)\npublic ");
        if( _model.isStatic ) {
          _os.write("static");
        }
        _os.write(" class ");
        _os.write(_model.className);
        _os.write(" {\n");
        for(int _i=0; _i< _model.properties.size(); _i+=1){
          render0(_os, _model.properties.get(_i), (_i+1) >= _model.properties.size());
        }
        _os.write("\n");
        for(int _i=0; _i< _model.types.size(); _i+=1){
          render1(_os, _model.types.get(_i), (_i+1) >= _model.types.size());
        }
        _os.write("}");
    }

}
