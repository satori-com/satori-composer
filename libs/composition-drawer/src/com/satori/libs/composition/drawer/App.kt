package com.satori.libs.composition.drawer

import com.mxgraph.util.*
import com.satori.libs.common.kotlin.json.*
import java.awt.*
import java.io.*
import javax.imageio.*

object App : IJsonContext by DefaultJsonContext {
  
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
    }.scope { toValue<AppArgs>() }
    
    val graph = createGraph(
      appArgs.cfgPath ?: throw Exception("missing --cfg-path"),
      appArgs.blockWidth, appArgs.blockHeight
    )
    val image = mxCellRenderer.createBufferedImage(graph, null, 1.0, Color.WHITE, true, null)
    ImageIO.write(image, appArgs.imgFormat, File(appArgs.imgPath))
  }
}
