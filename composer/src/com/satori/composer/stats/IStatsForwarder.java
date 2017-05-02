package com.satori.composer.stats;

import com.satori.composer.runtime.*;
import com.satori.mods.core.stats.*;

public interface IStatsForwarder extends IStatsCollector, IComposerRuntimeModule {
  void dispose();
  
  boolean disposed();
}
