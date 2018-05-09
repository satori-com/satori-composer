package com.satori.libs.gradle.plugin.processor

import com.google.auto.service.*
import com.satori.libs.gradle.plugin.annotations.*
import javax.annotation.processing.*
import javax.lang.model.*
import javax.lang.model.element.*
import javax.tools.*
import com.sun.tools.javac.code.*
import java.io.IOException
import javax.tools.StandardLocation
import javax.tools.FileObject
import java.util.HashSet
import java.util.SortedSet
import sun.tools.jstat.Alignment.keySet
import javax.annotation.processing.Filer



@AutoService(Processor::class)
class GradlePluginProcessor : AbstractProcessor() {
  
  override fun getSupportedAnnotationTypes(): Set<String> {
    return setOf(GradlePlugin::class.java.name)
  }
  
  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latestSupported()
  }
  
  inline fun<reified T: Annotation> Element.getAnnotation() : T{
    return getAnnotation(T::class.java)
  }
  
  override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    if (roundEnv.processingOver()) {
      return true
    }
    for( el in roundEnv.getElementsAnnotatedWith(GradlePlugin::class.java)){
      if(el !is TypeElement) throw Exception("type element expected")
      val pluginName = el.getAnnotation<GradlePlugin>().value
      generateFile(pluginName, el.qualifiedName.toString())
    }
    return true
  }
  
  fun generateFile(pluginName: String, className: String){
    val filer = processingEnv.filer
  
    val resourceFile = "META-INF/gradle-plugins/$pluginName.properties"
    val fileObject = filer.createResource(
      StandardLocation.CLASS_OUTPUT, "",resourceFile
    )
    logNote("generating file: '${fileObject.toUri()}'")
  
    fileObject.openWriter().use {writer ->
      writer.write("implementation-class=$className")
    }
  }
  
  fun logNote(msg: String) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
  }
  
}