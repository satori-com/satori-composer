package com.satori.libs.composition.drawer

object AppExample {
  
  @JvmStatic
  fun main(vararg args: String) {
    App.main(
      "--cfg-path", "mods-examples/big-blue-bus/res/com/satori/mods/resources/config.json",
      "--block-width", "100",
      "--block-height", "100",
      "--img-path", "big-blue-bus.png"
    )
  }
}
