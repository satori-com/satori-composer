import org.gradle.kotlin.dsl.*
import org.gradle.plugin.use.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
  kotlin("jvm", "1.1.50")
}

group = "com.satori"
version = "0.3.0-SNAPSHOT"

buildDir = file(".out")

java.sourceSets["main"].java.srcDir("src")
java.sourceSets["main"].resources.srcDir("res")

java.sourceSets["test"].java.srcDir("tests/src")
java.sourceSets["test"].resources.srcDir("tests/res")

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = JavaVersion.VERSION_1_8.toString()
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = JavaVersion.VERSION_1_8.toString()
}


repositories {
  mavenCentral()
}

val jacksonVer = "2.9.0"

dependencies {
  compile(kotlin("stdlib-jre8", "1.1.50"))
  compile("com.fasterxml.jackson.core:jackson-core:$jacksonVer")
  compile("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")
  compile("com.fasterxml.jackson.module:jackson-module-afterburner:$jacksonVer")
  compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVer")
}
