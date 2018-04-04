package com.satori.libs.composition.drawer

import com.mxgraph.util.*
import com.satori.libs.composition.drawer.json.*
import java.awt.*
import java.io.*
import javax.imageio.*

object App {
  
  @JvmStatic
  fun main(vararg args: String) {
    println(MetaInfo)
    
    val appArgs = jsonObject {
      val itor = args.iterator()
      while (true) {
        val name = if (itor.hasNext()) itor.next() else break
        val value = if (itor.hasNext()) itor.next() else throw Exception("argument value is missing")
        if (!name.startsWith("--")) throw Exception("argument '$name' should start with '--'")
        field(name.removePrefix("--"), value)
      }
    }.toValue<AppArgs>()
    
    val graph = createGraph(
      appArgs.cfgPath ?: throw Exception("missing --cfg-path"),
      appArgs.width, appArgs.height
    )
    val image = mxCellRenderer.createBufferedImage(graph, null, 1.0, Color.WHITE, true, null)
    ImageIO.write(image, "PNG", File(appArgs.imgPath))
  }
}
