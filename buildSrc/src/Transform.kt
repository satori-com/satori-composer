import groovy.lang.*
import groovy.text.*
import org.gradle.api.*
import java.io.*
import java.nio.charset.*
import java.nio.file.*

open class Transform {
  
  companion object {
  
    @JvmStatic
    fun execute(spec: ITransformSpec, project: Project) = execute(spec, project, null)
    
    @JvmStatic
    fun execute(spec: ITransformSpec, project: Project, model: Any?): Unit = spec.run {
      val engine = GStringTemplateEngine()
      if(template === null) throw Exception("'template' not specified")
      if(output === null) throw Exception("'output' not specified")
      println("transforming '${template}'-> '${output}'...")
      output!!.toPath().parent.toFile().mkdirs()
      OutputStreamWriter(Files.newOutputStream(output!!.toPath()), StandardCharsets.UTF_8).use { writer ->
        val scope = TemplateScope(engine, project, writer).apply {
          setProperty("template", template)
          setProperty("output", output)
          setProperty("project", project)
          setProperty("rootProject", project.rootProject)
          setProperty("env", System.getenv())
          if(model !== null){
            setProperty("model", model)
          }
        }
        execute(scope, template, engine, writer)
      }
    }
    
    @JvmStatic
    fun execute(scope: Any?, template: File?, engine: GStringTemplateEngine, writer: Writer) {
      val writable = engine.createTemplate(template).make()
      (writable as Closure<*>).delegate = scope
      writable.writeTo(writer)
    }
  }
}
