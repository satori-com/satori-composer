import com.mxgraph.util.*
import com.satori.libs.composition.drawer.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import java.awt.*
import java.io.*
import javax.imageio.*

open class GenerateCompositionDiagramTask : DefaultTask() {
  
  @InputFile
  var cfgPath: File? = null
  
  @OutputFile
  var imgPath: File = File(project.buildDir, "composition.png")
  
  var blockWidth: Double = CompositionGraphWriter.defaultWidth
  var blockHeight: Double = CompositionGraphWriter.defaultHeight
  
  @TaskAction
  fun generateImage() {
    println(MetaInfo)
  
    
    val cfgPath = cfgPath ?: throw Exception("cfgPath not specified")
  
    cfgPath.toPath().parent.toFile().mkdirs()
    val graph = createGraph(
      cfgPath ?: throw Exception("cfgPath not specified"),
      blockWidth, blockHeight
    )
    val image = mxCellRenderer.createBufferedImage(graph, null, 1.0, Color.WHITE, true, null)
    ImageIO.write(image, "PNG", imgPath)
  }
}

