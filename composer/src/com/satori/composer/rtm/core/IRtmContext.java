package com.satori.composer.rtm.core;

import com.satori.async.api.*;

import java.util.*;

import org.slf4j.*;

public interface IRtmContext {
  Logger log();
  
  String genUid();
  
  ArrayDeque<IAsyncHandler<Boolean>> onConnected();
  
  ArrayDeque<IAsyncHandler<Boolean>> onWritable();
  
}
    