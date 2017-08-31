package com.satori.mods.suite;

import java.util.*;

public class WellKnownMods implements IWellKnownMods {
  private final static HashMap<String, String> records;
  
  static {
    records = new HashMap<>();
    
    records.put("array-unwrap", ArrayUnwrapMod.class.getCanonicalName());
    records.put("dedup", DedupMod.class.getCanonicalName());
    records.put("gtfs-proto-buf-to-json", GtfsProtoBufToJsonMod.class.getCanonicalName());
    records.put("http-poll", HttpPollMod.class.getCanonicalName());
    records.put("http-post", HttpPostMod.class.getCanonicalName());
    records.put("printer", PrinterMod.class.getCanonicalName());
    records.put("rtm-publish", RtmPublishMod.class.getCanonicalName());
    records.put("rtm-subscribe", RtmSubscribeMod.class.getCanonicalName());
    records.put("stats-jvm", StatsJvmMod.class.getCanonicalName());
    records.put("ws-subscribe", WsSubscribeMod.class.getCanonicalName());
    records.put("xslt", XsltMod.class.getCanonicalName());
    records.put("queue", QueueMod.class.getCanonicalName());
    records.put("barrier", BarrierMod.class.getCanonicalName());
  }
  
  // IWellKnownMods implementation
  
  @Override
  public String resolve(String shortName){
    return records.get(shortName);
  }
}
