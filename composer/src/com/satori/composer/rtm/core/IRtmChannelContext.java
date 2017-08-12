package com.satori.composer.rtm.core;

import java.util.*;

public interface IRtmChannelContext extends IRtmContext {
  
  String channel();
  
  String filter();
  
  Map history();
}
    