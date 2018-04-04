package com.satori.libs.composition.drawer

import com.mxgraph.swing.*
import java.awt.*
import javax.swing.*

object UIAppExample : JFrame("big-blue-bus composition") {
  
  init {
    val graph = createGraph("mods-examples/big-blue-bus/res/com/satori/mods/resources/config.json")
    contentPane.add(
      mxGraphComponent(graph), BorderLayout.CENTER
    )
  }
  
  @JvmStatic
  fun main(vararg args: String) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(960, 720);
    setVisible(true);
  }
}
