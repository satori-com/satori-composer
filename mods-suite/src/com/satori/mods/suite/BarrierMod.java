package com.satori.mods.suite;

import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class BarrierMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(BarrierMod.class);
  
  private final BarrierModStats stats = new BarrierModStats();
  private final int pauseThreshold;
  private final int resumeThreshold;
  
  private int counter;
  private boolean paused = false;
  private final ArrayDeque<AsyncFuture> resumeFutures;
  
  public BarrierMod() throws Exception {
    this(
      BarrierModSettings.defaultPauseThreshold,
      BarrierModSettings.defaultResumeThreshold(BarrierModSettings.defaultPauseThreshold)
    );
  }
  
  public BarrierMod(JsonNode config) throws Exception {
    this(Config.parseAndValidate(config, BarrierModSettings.class));
  }
  
  public BarrierMod(BarrierModSettings config) {
    this(config.pauseThreshold, config.resumeThreshold);
  }
  
  public BarrierMod(int pauseThreshold, int resumeThreshold) {
    this.pauseThreshold = pauseThreshold;
    this.resumeThreshold = resumeThreshold;
    counter = 0;
    resumeFutures = new ArrayDeque<>();
  }
  
  // IMod implementation
  
  @Override
  public void onStop() throws Exception {
    super.onStop();
    stats.suppress();
    counter = 0;
    log.info("stopped");
  }
  
  @Override
  public void onStart() throws Exception {
    log.info("started");
  }
  
  @Override
  public void onPulse() {
    log.debug("pulse");
    stats.infly.aggregate(counter);
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void onInput(String inputName, JsonNode data, IAsyncHandler cont) throws Exception {
    IAsyncFuture future = onInput(inputName, data);
    future.onCompleted(cont);
  }
  
  @Override
  public IAsyncFuture onInput(String inputName, JsonNode data) throws Exception {
    stats.recv += 1;
    processData(data);
    if(!paused){
      if(counter < pauseThreshold){
        return AsyncResults.succeeded();
      }
      paused = true;
      stats.paused += 1;
    }
  
    AsyncFuture future = new AsyncFuture();
    resumeFutures.addLast(future);
    return future;
  }
  
  // private methods
  
  private void onSendCompleted(IAsyncResult ar) {
    counter -= 1;
    if (ar.isFailed()) {
      log.warn("failed to process message", ar.getError());
    }
    resumeIfNeeded();
  }
  
  @SuppressWarnings("unchecked")
  private void processData(JsonNode data) {
    counter += 1;
    final IAsyncFuture future;
    try {
      stats.sent += 1;
      future = yield(data);
    } catch (Throwable e) {
      counter -= 1;
      log.warn("failed to process message", e);
      return;
    }
    if(future == null){
      counter -= 1;
      log.error("internal error", new NullPointerException());
      return;
    }
    try {
      if (!future.isCompleted()) {
        future.onCompleted(this::onSendCompleted);
        return;
      }
      onSendCompleted(future.getResult());
    } catch (Throwable e) {
      log.error("internal error", e);
      // TODO: fast fail?
    }
  }
  
  private void resumeIfNeeded() {
    while (true){
      if(paused){
        if(counter > resumeThreshold){
          return;
        }
        paused = false;
        stats.resumed += 1;
      }
      AsyncFuture future = resumeFutures.pollFirst();
      if(future == null){
        return;
      }
      try {
        future.succeed();
      } catch (Throwable e){
        log.warn("continuation failure", e);
      }
    }
  }
}
