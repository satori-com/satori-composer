package com.satori.codegen.jpoet.dsl.scopes

import com.satori.codegen.utils.*

@IScope.Marker
interface IScope : ICodeFormatter {
  @DslMarker
  annotation class Marker
}
