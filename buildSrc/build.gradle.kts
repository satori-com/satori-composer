import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm", "1.1.50")
    java
}

group = "com.satori"
version = "0.3.0-SNAPSHOT"

buildDir = file(".out")

java.sourceSets["main"].java.srcDir("src")
java.sourceSets["main"].resources.srcDir("res")

java.sourceSets["test"].java.srcDir("tests/src")
java.sourceSets["test"].resources.srcDir("tests/res")

dependencies {
    compile(kotlin("stdlib-jre8", "1.1.50"))
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
}
