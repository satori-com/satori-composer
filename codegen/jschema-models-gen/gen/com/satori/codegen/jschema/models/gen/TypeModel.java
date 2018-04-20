//auto generated
//don't modify


package com.satori.codegen.jschema.models.gen;

import java.util.ArrayList;

public class TypeModel {

    public final TypeModel _root;
    public boolean isStatic;
    public final ArrayList<TypesItem> types = new ArrayList<TypesItem>();
    public String className;
    public final ArrayList<PropertiesItem> properties = new ArrayList<PropertiesItem>();

    public TypeModel() {
        _root = this;
    }

    public TypesItem addTypesItem() {
        TypesItem _item = new TypesItem(_root, this);
        types.add(_item);
        return _item;
    }

    public PropertiesItem addPropertiesItem() {
        PropertiesItem _item = new PropertiesItem(_root, this);
        properties.add(_item);
        return _item;
    }

    public class PropertiesItem {

        public final TypeModel _root;
        public final TypeModel _parent;
        public String varName;
        public String schemaName;
        public String type;

        public PropertiesItem(TypeModel _root, TypeModel _parent) {
            this._root = _root;
            this._parent = _parent;
        }

    }

    public class TypesItem {

        public final TypeModel _root;
        public final TypeModel _parent;
        public TypeModel type;

        public TypesItem(TypeModel _root, TypeModel _parent) {
            this._root = _root;
            this._parent = _parent;
        }

    }

}
