package com.satori.libs.gradle.utils

import org.gradle.api.plugins.*

inline fun <reified T> PluginManager.apply() = apply(T::class.java)