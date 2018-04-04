package com.satori.libs.composition.drawer

import com.mxgraph.layout.*
import com.mxgraph.layout.hierarchical.*
import com.mxgraph.util.*
import com.mxgraph.view.*
import com.satori.composer.runtime.*
import com.satori.mods.core.config.*
import com.satori.mods.suite.*
import java.io.*

fun ICompositionWriter.addComposition(mods: HashMap<String, CompositionNodeConfig>) {
  mods.forEach { id, mod ->
    addMod(id, mod)
  }
  
  mods.forEach { to, mod ->
    mod.connectors?.forEach { input, ids ->
      ids.forEach { from ->
        linkMods(from, to)
      }
    }
  }
}

private fun mxGraph.applyDefaultStylesheet() {
  val stylesheet = mxStylesheet()
  stylesheet.defaultEdgeStyle = HashMap<String, Any>().also { edge ->
    // Settings for edges
    edge[mxConstants.STYLE_ROUNDED] = true
    edge[mxConstants.STYLE_ORTHOGONAL] = true
    edge[mxConstants.STYLE_EDGE] = "elbowEdgeStyle"
    edge[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_CONNECTOR
    edge[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_CLASSIC
    edge[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE
    edge[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER
    edge[mxConstants.STYLE_STROKECOLOR] = "#000000" // default is #6482B9
    edge[mxConstants.STYLE_FONTCOLOR] = "#446299"
  }
  setStylesheet(stylesheet)
}

fun createGraph(mods: HashMap<String, CompositionNodeConfig>, width: Double, height: Double): mxGraph {
  val graph = mxGraph()
  
  graph.model.beginUpdate()
  CompositionGraphWriter(graph, width, height).addComposition(mods)
  graph.model.endUpdate()
  
  
  graph.applyDefaultStylesheet()
  mxHierarchicalLayout(graph).execute(graph.getDefaultParent())
  mxParallelEdgeLayout(graph).execute(graph.getDefaultParent())
  
  //val layout = mxFastOrganicLayout(graph)
  //val layout = mxOrganicLayout(graph)
  //val layout = mxCompactTreeLayout(graph)
  
  return graph
}

fun createGraph(cfgPath: File, width: Double, height: Double): mxGraph {
  val cfg = cfgPath.inputStream().use { inputStream ->
    Config.mapper.readValue(inputStream, ComposerRuntimeConfig::class.java).apply {
      validate()
    }
  }
  return createGraph(cfg.mods, width, height)
}

fun createGraph(cfgPath: String) = createGraph(
  cfgPath, CompositionGraphWriter.defaultWidth, CompositionGraphWriter.defaultHeight
)

fun createGraph(cfgPath: String, width: Double, height: Double) = createGraph(
  File(cfgPath), CompositionGraphWriter.defaultWidth, CompositionGraphWriter.defaultHeight
)

