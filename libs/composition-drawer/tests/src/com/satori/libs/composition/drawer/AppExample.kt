package com.satori.libs.composition.drawer

object AppExample {
  
  @JvmStatic
  fun main(vararg args: String) {
    App.main(
      "--cfg-path", "mods-examples/big-blue-bus/res/com/satori/mods/resources/config.json",
      "--image-path", "big-blue-bus.png"
    )
  }
}
