import groovy.text.*
import org.gradle.api.*
import org.gradle.api.tasks.*

class TemplateTask extends DefaultTask {
  TemplateEngine engine = new GStringTemplateEngine()

  @InputFile
  File template

  @OutputFile
  File output

  Map params = [:]

  TemplateTask() {
    params['env'] = System.getenv()
    params['project'] = project
    params['rootProject'] = project.rootProject

    /*println "[environment variables]"
    params['env'].each { k, v ->
      params[k] = v
      println "$k='$v'"
    }*/
  }

  @TaskAction
  def process() {
    println "transforming '${template}'-> '${output}'..."
    def writable = engine.createTemplate(template).make()
    writable.setDelegate(params)

    params['include'] = { fileName ->
      def w = engine.createTemplate(project.file(fileName)).make()
      w.setDelegate(params)
      return w.toString();
    }

    output.withWriter {
      writable.writeTo(it)
    }
  }
}