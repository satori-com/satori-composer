package com.satori.mods.suite;

import com.satori.libs.async.api.*;
import com.satori.mods.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class DedupMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(DedupMod.class);
  
  private final DedupModStats stats = new DedupModStats();
  private final HashMap<Object, Long> cache = new HashMap<>();
  private final long expirationInterval;
  private final boolean override;
  private final String keySelector;
  private long nextGc = Long.MAX_VALUE;
  
  public DedupMod(JsonNode config) throws Exception {
    this(Config.parseAndValidate(config, DedupModSettings.class));
  }
  
  public DedupMod(DedupModSettings config) {
    expirationInterval = config.expirationInterval;
    override = config.override;
    keySelector = config.keySelector;
    log.info("created");
  }
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    cache.clear();
    stats.reset();
    nextGc = timestamp() + expirationInterval;
    log.info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    nextGc = Long.MAX_VALUE;
    stats.reset();
    cache.clear();
    log.info("terminated");
  }
  
  @Override
  public void onPulse() {
    log.debug("pulse received");
    long ts = timestamp();
    if (ts > nextGc) {
      nextGc = ts + expirationInterval;
      performGc(ts);
    }
    stats.cached.aggregate(cache.size());
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onInput(String input, JsonNode data, IAsyncHandler cont) throws Exception {
    stats.recv += 1;
    final long expired = timestamp() + expirationInterval;
    if (cache(data, expired) != null) {
      // filter out repeated entity
      stats.filtered += 1;
      cont.succeed();
      return;
    }
    stats.sent += 1;
    yield(data, cont);
  }
  
  @Override
  public IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    stats.recv += 1;
    final long expired = timestamp() + expirationInterval;
    if (cache(data, expired) != null) {
      // filter out repeated entity
      stats.filtered += 1;
      return AsyncResults.succeededNull;
    }
    stats.sent += 1;
    return yield(data);
  }
  
  // private methods
  
  private Long cache(JsonNode msg, Long expired) {
    final Long cached;
    final Object key = getKey(msg);
    if (key == null) {
      return null;
    }
    if (!override) {
      cached = cache.putIfAbsent(key, expired);
    } else {
      cached = cache.put(key, expired);
    }
    return cached;
  }
  
  private JsonNode getKey(JsonNode msg) {
    if (keySelector == null || keySelector.isEmpty()) {
      return msg;
    }
    if (msg == null) {
      return null;
    }
    JsonNode key = msg.at(keySelector);
    if (key.isMissingNode()) {
      return null;
    }
    return key;
  }
  
  private void performGc(long ts) {
    log.debug("performing garbage collection");
    Iterator<Map.Entry<Object, Long>> itor = cache.entrySet().iterator();
    while (itor.hasNext()) {
      Map.Entry<Object, Long> entry = itor.next();
      if (ts > entry.getValue()) {
        
        itor.remove();
        stats.expired += 1;
        log.debug("entity expired ({})", entry.getKey());
      }
    }
  }
  
  private static long timestamp() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
  
}
