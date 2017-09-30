package com.satori.mods.suite;

import com.satori.mods.core.async.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

public class QueueMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(QueueMod.class);
  
  private final QueueModStats stats = new QueueModStats();
  private final ArrayDeque<JsonNode> queue;
  private final int pauseThreshold;
  private final int resumeThreshold;
  
  private IAsyncFuture sendingFuture = null;
  private final ArrayDeque<AsyncFuture> resumeFutures;
  private boolean paused = false;
  
  public QueueMod() throws Exception {
    this(
      QueueModSettings.defaultPauseThreshold,
      QueueModSettings.defaultResumeThreshold(QueueModSettings.defaultPauseThreshold)
    );
  }
  
  public QueueMod(JsonNode config) throws Exception {
    this(Config.parseAndValidate(config, QueueModSettings.class));
  }
  
  public QueueMod(QueueModSettings config) {
    this(config.pauseThreshold, config.resumeThreshold);
  }
  
  public QueueMod(int pauseThreshold, int resumeThreshold) {
    this.pauseThreshold = pauseThreshold;
    this.resumeThreshold = resumeThreshold;
    queue = new ArrayDeque<>(pauseThreshold);
    resumeFutures = new ArrayDeque<>();
  }
  
  // IMod implementation
  
  
  @Override
  public void onStop() throws Exception {
    super.onStop();
    stats.suppress();
    queue.clear();
    log.info("stopped");
  }
  
  @Override
  public void onStart() throws Exception {
    log.info("started");
  }
  
  @Override
  public void onPulse() {
    log.debug("pulse");
    stats.queued.aggregate(queue.size());
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
    queue.addLast(data);
    if(!paused){
      if(queue.size() < pauseThreshold){
        if (sendingFuture == null) {
          processQueue();
        }
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
    sendingFuture = null;
    if (!ar.isSucceeded()) {
      log.warn("failed to process message", ar.getError());
    }
    processQueue();
    resumeIfNeeded();
  }
  
  @SuppressWarnings("unchecked")
  private void processQueue() {
    while (queue.size() > 0) {
      try {
        JsonNode data = queue.pollFirst();
        stats.sent += 1;
        sendingFuture = yield(data);
      } catch (Throwable e) {
        sendingFuture = null;
        log.warn("failed to process message", e);
        continue;
      }
      try {
        if (!sendingFuture.isCompleted()) {
          sendingFuture.onCompleted(this::onSendCompleted);
          return;
        }
        // completed immediately
        IAsyncResult<?> ar = sendingFuture.getResult();
        if (!ar.isSucceeded()) {
          log.warn("failed to process message", ar.getError());
        }
      } catch (Throwable e) {
        log.warn("failed to process message", e);
      }
      sendingFuture = null;
    }
  }
  
  
  private void resumeIfNeeded() {
    while (true){
      if(paused && queue.size() > resumeThreshold){
        return;
      }
      if(paused) {
        stats.resumed += 1;
        paused = false;
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
  
  private static long timestamp() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
  
}
