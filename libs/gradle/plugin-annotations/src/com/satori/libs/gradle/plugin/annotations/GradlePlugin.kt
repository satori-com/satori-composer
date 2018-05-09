package com.satori.libs.gradle.plugin.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GradlePlugin(val value: String)