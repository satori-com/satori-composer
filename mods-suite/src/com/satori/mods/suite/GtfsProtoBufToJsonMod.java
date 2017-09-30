package com.satori.mods.suite;

import com.satori.mods.api.*;
import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;
import com.satori.mods.suite.gtfs.*;

import com.fasterxml.jackson.databind.*;
import com.google.transit.realtime.*;
import org.slf4j.*;

public class GtfsProtoBufToJsonMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(GtfsProtoBufToJsonMod.class);
  
  private final GtfsProtoBufToJsonModStats stats = new GtfsProtoBufToJsonModStats();
  private final JsonNode userData;
  
  public GtfsProtoBufToJsonMod(JsonNode config) throws Exception {
    this(Config.parseAndValidate(config, GtfsProtoBufToJsonModSettings.class));
  }
  
  public GtfsProtoBufToJsonMod(GtfsProtoBufToJsonModSettings config) {
    userData = config.userData;
    log.info("created");
  }
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    stats.reset();
    log.info("initialized");
  }
  
  @Override
  public void dispose() throws Exception {
    super.dispose();
    stats.reset();
    log.info("terminated");
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onInput(String input, JsonNode data, IAsyncHandler cont) throws Exception {
    stats.recv += 1;
    GtfsFeedMessage feed = GtfsProtoBufConverter.convert(
      GtfsRealtime.FeedMessage.parseFrom(data.binaryValue())
    );
    GtfsFeedHeader header = feed.header;
    GtfsFeedEntity[] entities = feed.entity;
    if (userData != null) {
      if (header == null) {
        header = new GtfsFeedHeader();
      }
      header.ext("user-data", userData);
    }
    if (entities == null || entities.length <= 0) {
      cont.succeed();
      return;
    }
    yieldLoop(0, header, entities, cont);
  }
  
  // private methods
  
  public void yieldLoop(int i, GtfsFeedHeader header, GtfsFeedEntity[] entities, IAsyncHandler cont) throws Exception {
    while (true) {
      stats.sent += 1;
      GtfsFeedEntity entity = entities[i++];
      GtfsFeedMessage gtfsFeedMessage = new GtfsFeedMessage();
      gtfsFeedMessage.header = header;
      gtfsFeedMessage.entity = new GtfsFeedEntity[]{entity};
      final JsonNode msg = Config.mapper.valueToTree(gtfsFeedMessage);
      if (i >= entities.length) {
        // this the last message
        yield(msg, cont);
        return;
      }
      IAsyncFuture<?> future = yield(msg);
      if (!future.isCompleted()) {
        // operation still in progress, set continuation and exit
        int next = i;
        future.onCompleted(ar -> {
          if (!ar.isSucceeded()) {
            cont.fail(ar.getError());
            return;
          }
          try {
            yieldLoop(next, header, entities, cont);
          } catch (Exception e) {
            cont.fail(e);
          }
        });
        return;
      }
      // operation was completed immediately
      IAsyncResult<?> ar = future.getResult();
      if (!ar.isSucceeded()) {
        // log error if processing failed
        log.warn("failed to process entity", ar.getError());
      }
    }
  }
}
