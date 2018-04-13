package com.satori.codegen.jpoet.dsl.extensions

import com.satori.codegen.jpoet.dsl.*
import com.satori.codegen.jpoet.dsl.scopes.*

fun IHasAccessModifier.PUBLIC() = ACCESS(AccessModifier.PUBLIC)
fun IHasAccessModifier.PRIVATE() = ACCESS(AccessModifier.PRIVATE)
fun IHasAccessModifier.PROTECTED() = ACCESS(AccessModifier.PROTECTED)
