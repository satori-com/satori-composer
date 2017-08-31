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
  private AsyncFuture resumeFuture = null;
  
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
    if (queue.size() < pauseThreshold) {
      if (sendingFuture == null) {
        processQueue();
      }
      return AsyncResults.succeeded();
    }
    if (resumeFuture == null) {
      resumeFuture = new AsyncFuture();
    }
    return resumeFuture;
  }
  
  // private methods
  
  private void onSendCompleted(IAsyncResult ar) {
    sendingFuture = null;
    if (ar.isFailed()) {
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
        sendingFuture = yield(data);
        if (!sendingFuture.isCompleted()) {
          sendingFuture.onCompleted(this::onSendCompleted);
          return;
        }
        // completed immediately
        if (sendingFuture.isFailed()) {
          log.warn("failed to process message", sendingFuture.getError());
        }
        sendingFuture = null;
      } catch (Throwable e) {
        log.warn("failed to process message", e);
      }
    }
  }
  
  
  private void resumeIfNeeded() {
    AsyncFuture resumeFuture = this.resumeFuture;
    if (resumeFuture != null && queue.size() <= resumeThreshold) {
      this.resumeFuture = null;
      resumeFuture.succeed();
    }
  }
  
  private static long timestamp() {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }
  
}
