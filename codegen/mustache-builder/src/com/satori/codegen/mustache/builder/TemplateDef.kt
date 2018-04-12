package com.satori.codegen.mustache.builder

class TemplateDef(val name: String) {
  val rootModel = SchemaNode.newRootNode(name)
}
