
package com.satori.codegen.jschema.models.gen;

import java.io.Writer;


/**
 * auto generated
 * don't modify
 * 
 */
public class FileTemplate {


    public static void render0(Writer _os, String _model, boolean _last)
        throws Exception
    {
        _os.write("import ");
        _os.write(_model);
        _os.write(";\n");
    }

    public static void render(Writer _os, FileModel _model)
        throws Exception
    {
        _os.write("// auto generated\n// don't modify\npackage ");
        _os.write(_model.packageName);
        _os.write(";\n\n\nimport com.fasterxml.jackson.annotation.*;\n\n");
        for(int _i=0; _i< _model.imports.size(); _i+=1){
          render0(_os, _model.imports.get(_i), (_i+1) >= _model.imports.size());
        }
        _os.write("\n");
        TypeTemplate.render(_os, _model.type);
    }

}
