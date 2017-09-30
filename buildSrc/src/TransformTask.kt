import groovy.lang.*
import groovy.text.*
import org.gradle.api.*
import org.gradle.api.internal.file.*
import org.gradle.api.tasks.*
import java.io.*
import java.nio.charset.*
import java.nio.file.*
import java.util.*

open class TransformTask : DefaultTask(), ITransformSpec {

  @InputFile
  override var template: File? = null

  @OutputFile
  override var output: File? = null

  @TaskAction
  fun process() {
    Transform.execute(this, project)
  }
}
