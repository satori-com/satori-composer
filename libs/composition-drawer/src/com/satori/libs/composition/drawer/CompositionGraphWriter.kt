package com.satori.libs.composition.drawer

import com.mxgraph.model.*
import com.mxgraph.view.*
import com.satori.mods.suite.*

class CompositionGraphWriter(val graph: mxGraph, val width: Double = defaultWidth, val height: Double = defaultHeight) : ICompositionWriter {
  
  override fun addMod(id: String, cfg: ModConfig) {
    graph.insertVertex(id, "$id\n(${cfg.type})")
  }
  
  override fun linkMods(from: String, to: String) {
    graph.insertEdge(from, to)
  }
  
  private fun mxGraph.insertVertex(id: String, label: String): Any {
    val v = graph.insertVertex(defaultParent, id, label, 0.0, 0.0, width, height)
    return v
  }
  
  private fun mxGraph.insertEdge(fromId: String, toId: String): Any {
    val v1 = (model as mxGraphModel).getCell(fromId)
    val v2 = (model as mxGraphModel).getCell(toId)
    return insertEdge(defaultParent, null, null, v1, v2)
  }
  
  private fun mxGraph.insertEdge(fromCell: Any, toCell: Any): Any {
    return insertEdge(defaultParent, null, null, fromCell, toCell)
  }
  
  companion object {
    val defaultWidth: Double = 260.0
    val defaultHeight: Double = 40.0
  }
}
