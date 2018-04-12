package com.satori.codegen.codemodel.dsl.builders

import com.satori.codegen.codemodel.dsl.traits.*
import com.sun.codemodel.*

open class BaseBuilder(jmodel: JCodeModel) : IScopeTrait {
  override final val jmodel = jmodel
}
