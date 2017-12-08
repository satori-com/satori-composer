package com.satori.composer.rtm.core;

import java.util.*;

public interface IRtmChannelContext extends IRtmContext {
  
  String channel();
  
  boolean prefix();
  
  String filter();
  
  Map history();
}
    