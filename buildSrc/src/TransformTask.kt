import org.gradle.api.*
import org.gradle.api.tasks.*
import java.io.*

open class TransformTask : DefaultTask(), ITransformSpec {
  
  @InputFile
  override var template: File? = null
  
  @OutputFile
  override var output: File? = null
  
  var model = HashMap<String, Any>()
  
  
  @TaskAction
  fun process() {
    Transform.execute(this, project, model)
  }
}
