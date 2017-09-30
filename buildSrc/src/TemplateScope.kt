import groovy.lang.*
import groovy.text.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import java.io.*
import java.nio.charset.*
import java.nio.file.*
import java.util.HashMap

open class TemplateScope(val engine: GStringTemplateEngine, val project: Project, val writer: Writer) : GroovyObjectSupport() {
  val props = HashMap<String, Any?>()

  override fun invokeMethod(name: String, args: Any?): Any? {
    when (name) {
      "include" -> {
        val arg = (args!! as Array<*>)[0]
        val file = when (arg) {
          is File -> arg
          is String -> project.file(arg)
          else -> {
            return super.invokeMethod(name, args)
          }
        }
        Transform.execute(this, file, engine, writer)
        return null// writable.toString()
      }
      else -> {
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