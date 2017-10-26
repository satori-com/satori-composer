package com.satori.mods.suite;

import com.satori.mods.api.*;

import java.util.*;

public class WellKnownMods implements IWellKnownMods {
  private final static HashMap<String, IModFactory> records;
  
  static {
    records = new HashMap<>();
    
    records.put("array-unwrap", ArrayUnwrapMod::new);
    records.put("dedup", DedupMod::new);
    records.put("gtfs-proto-buf-to-json", GtfsProtoBufToJsonMod::new);
    records.put("http-poll", HttpPollMod::new);
    records.put("http-post", HttpPostMod::new);
    records.put("printer", c -> new PrinterMod());
    records.put("rtm-publish", RtmPublishMod::new);
    records.put("rtm-subscribe", RtmSubscribeMod::new);
    records.put("stats-jvm", c -> new StatsJvmMod());
    records.put("ws-subscribe", WsSubscribeMod::new);
    records.put("xslt", XsltMod::new);
    records.put("queue", QueueMod::new);
    records.put("barrier", BarrierMod::new);
  }
  
  // IWellKnownMods implementation
  
  @Override
  public IModFactory resolve(String shortName) {
    return records.get(shortName);
  }
}
