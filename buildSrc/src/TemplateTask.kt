import groovy.lang.*
import groovy.text.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import java.io.*
import java.nio.charset.*
import java.nio.file.*
import java.util.*

open class TemplateTask : DefaultTask() {
  val engine = GStringTemplateEngine()

  @InputFile
  var template: File? = null

  @OutputFile
  var output: File? = null

  class TemplateScope(val engine:GStringTemplateEngine, val project: Project, val writer:Writer, val task:TemplateTask): GroovyObjectSupport() {
    val props = HashMap<String, Any?>()

    init {
      props.put("env", System.getenv())
      props.put("project", project)
      props.put("rootProject", project.rootProject)
      props.put("template", task.template)
      props.put("output", task.output)

      /*println "[environment variables]"
      params['env'].each { k, v ->
        params[k] = v
        println "$k='$v'"
      }*/
    }
    override fun invokeMethod(name: String, args: Any?): Any? {
      when(name){
        "include" -> {
          val arg = (args!! as Array<*>)[0]
          val file = when(arg){
            is File -> {arg}
            is String -> project.file(arg)
            else -> {
              return super.invokeMethod(name, args)
            }
          }
          val writable = engine.createTemplate(file).make()
          (writable as Closure<*>).setDelegate(this)
          writable.writeTo(writer)
          return null// writable.toString()
        }
        else ->{
          return super.invokeMethod(name, args)
        }
      }
    }

    override fun setProperty(key: String, value: Any?) {
      props.put(key, value)
    }

    override fun getProperty(key: String): Any? {
      return props.get(key)
    }
  }



  @TaskAction
  fun process() {
    println("transforming '${template}'-> '${output}'...")
    OutputStreamWriter(Files.newOutputStream(output!!.toPath()), StandardCharsets.UTF_8).use { writer ->
      val writable = engine.createTemplate(template).make()

      val scope = TemplateScope(engine, project, writer, this)

      (writable as Closure<*>).delegate = scope
      writable.writeTo(writer)
    }

  }
}
