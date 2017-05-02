package com.satori.composer.rtm.core;

import com.satori.mods.core.async.*;

import java.util.*;

import org.slf4j.*;

public interface IRtmContext {
  Logger log();
  
  String genUid();
  
  ArrayDeque<IAsyncHandler<Boolean>> onConnected();
  
  ArrayDeque<IAsyncHandler<Boolean>> onWritable();
  
}
    