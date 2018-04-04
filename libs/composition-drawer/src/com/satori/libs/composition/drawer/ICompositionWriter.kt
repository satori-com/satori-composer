package com.satori.libs.composition.drawer

import com.satori.mods.suite.*

interface ICompositionWriter {
  fun addMod(id: String, cfg: ModConfig)
  fun linkMods(from: String, to: String)
}
