package com.satori.mods.suite;

import com.satori.composer.runtime.*;
import com.satori.libs.async.api.*;
import com.satori.libs.async.core.*;
import com.satori.mods.api.*;
import com.satori.mods.core.config.*;
import com.satori.mods.core.stats.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import io.netty.handler.codec.http.*;
import io.vertx.core.buffer.*;
import io.vertx.core.http.*;
import io.vertx.core.http.impl.*;
import io.vertx.core.http.impl.ws.*;
import org.slf4j.*;

public class WsSubscribeMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(WsSubscribeMod.class);
  public static final long INVALID_TIMER = Long.MIN_VALUE;
  
  public final WsSubscribeModStats stats = new WsSubscribeModStats();
  private WebSocket ws = null;
  private final WsSubscribeModSettings config;
  private final String path;
  private final long idleTimeout;
  private final long errorDelay;
  private final CaseInsensitiveHeaders headers = new CaseInsensitiveHeaders();
  private final IBufferParser bufferParser;
  private long timer = INVALID_TIMER;
  private long nextIdleTimeout = Long.MAX_VALUE;
  
  
  public WsSubscribeMod(JsonNode userData) throws Exception {
    this(Config.parseAndValidate(userData, WsSubscribeModSettings.class));
  }
  
  public WsSubscribeMod(WsSubscribeModSettings config) throws Exception {
    this.config = config;
    QueryStringEncoder qenc = new QueryStringEncoder(config.path);
    for (HashMap.Entry<String, String> e : config.args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    path = qenc.toString();
    if (config.headers != null) {
      for (HashMap.Entry<String, String> e : config.headers.entrySet()) {
        String key = e.getKey();
        String val = e.getValue();
        if (val == null || val.isEmpty()) {
          continue;
        }
        headers.add(key, val);
      }
    }
    errorDelay = config.errorDelay;
    idleTimeout = config.idleTimeout * 1000;
    if (config.format == null || config.format.isEmpty()) {
      bufferParser = this::processJsonContent;
    } else switch (config.format.toLowerCase()) {
      case "binary":
        bufferParser = this::processBinaryContent;
        break;
      case "json":
        bufferParser = this::processJsonContent;
        break;
      case "text":
        bufferParser = this::processTextContent;
        break;
      default:
        throw new RuntimeException("unsupported data format: " + config.format);
    }
    log.info("ws-subscriber mod created ({})", path);
  }
  
  // IMod implementation
  
  @Override
  public void init(final IModContext context) throws Exception {
    super.init(context);
    stats.reset();
    log.info("ws-subscriber mod initialized ({})", path);
    nextIdleTimeout = Long.MAX_VALUE;
    connect();
  }
  
  @Override
  public void dispose() throws Exception {
    if (timer != INVALID_TIMER) {
      vertx().cancelTimer(timer);
      timer = INVALID_TIMER;
    }
    stats.reset();
    nextIdleTimeout = Long.MAX_VALUE;
    if (ws != null) {
      ws.close();
    }
    log.info("ws-subscriber mod terminated ({})", path);
    super.dispose();
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    log.debug("collecting statistic...");
    stats.drain(collector);
  }
  
  @Override
  public void onPulse() {
    log.debug("pulse received");
    stats.pulse += 1;
    long ts = Stopwatch.timestamp();
    if (ws != null) {
      if (ts > nextIdleTimeout) {
        WebSocket ws = this.ws;
        this.ws = null;
        nextIdleTimeout = Long.MAX_VALUE;
        log.warn("no data, reconnecting....");
        try {
          ws.close();
        } catch (Exception e) {
          log.error("failed to close websocket", e);
        }
        connect();
      } else {
        final WebSocketFrameImpl pingFrame = new WebSocketFrameImpl(FrameType.PING);
        try {
          ws.writeFrame(pingFrame);
        } catch (Exception cause) {
          log.error("failed to send pong", cause);
        } finally {
          pingFrame.release();
        }
        final WebSocketFrameImpl pongFrame = new WebSocketFrameImpl(FrameType.PONG);
        try {
          ws.writeFrame(pongFrame);
        } catch (Exception cause) {
          log.error("failed to send pong", cause);
        } finally {
          pongFrame.release();
        }
      }
    }
  }
  
  // private methods
  
  public void connect() {
    log.info("connecting ({})", path);
    if (this.ws != null) {
      log.error("this.ws!= null", new CheckFailedException());
      return;
    }
    HttpClient http = createHttpClient();
    http.websocket(
      path, headers, this::onConnected, this::onConnectFailed
    );
  }
  
  public void onConnectFailed(Throwable cause) {
    log.warn("failed to connect", cause);
    if (!checkState(null)) {
      return;
    }
    nextIdleTimeout = Long.MAX_VALUE;
    timer = vertx().setTimer(
      errorDelay, this::onReconnect
    );
  }
  
  public void onReconnect(final long timer) {
    if (this.timer != timer) {
      log.error("this.timer != timer", new CheckFailedException());
      return;
    }
    this.timer = INVALID_TIMER;
    connect();
  }
  
  public void onConnected(WebSocket ws) {
    log.info("connected ({})", path);
    if (!checkState(null)) {
      return;
    }
    long ts = Stopwatch.timestamp();
    nextIdleTimeout = ts + idleTimeout;
    ws.frameHandler(f -> onFrameReceived(ws, f));
    ws.endHandler(u -> onDisconnected(ws, ts));
    this.ws = ws;
  }
  
  public void onDisconnected(WebSocket ws, long connectedTs) {
    log.info("disconnected ({})", path);
    if (!checkState(ws)) {
      return;
    }
    this.ws = null;
    nextIdleTimeout = Long.MAX_VALUE;
    long ts = Stopwatch.timestamp();
    long elapsed = ts - connectedTs;
    if (elapsed > config.errorDelay) {
      connect();
      return;
    }
    timer = vertx().setTimer(
      config.errorDelay, this::onReconnect
    );
  }
  
  private void onFrameReceived(WebSocket ws, WebSocketFrame frame) {
    log.debug("frame received ({})", path);
    if (!checkState(ws)) {
      return;
    }
    stats.recv += 1;
    nextIdleTimeout = Stopwatch.timestamp() + idleTimeout;
    IAsyncPromise promise = AsyncPromise.from(this::onMessageConsumed);
    try {
      if (!frame.isFinal()) {
        throw new Exception("continuation frames not supported yet");
      }
      JsonNode content = bufferParser.parse(frame.binaryData());
      yield(content, promise);
    } catch (Exception cause) {
      promise.fail(cause);
    }
  }
  
  private boolean checkState(WebSocket ws) {
    if (this.ws != ws) {
      if (this.ws != null) {
        log.error("this.ws != ws", new CheckFailedException());
      }
      return false;
    }
    if (timer != INVALID_TIMER) {
      log.error("timer != INVALID_TIMER", new CheckFailedException());
      vertx().cancelTimer(timer);
      timer = INVALID_TIMER;
    }
    return true;
  }
  
  private void onMessageConsumed(IAsyncResult ar) {
    if (!ar.isSucceeded()) {
      log.warn("processing message error", ar.getError());
      stats.failed += 1;
    } else {
      stats.succeeded += 1;
    }
  }
  
  private JsonNode processJsonContent(Buffer buf) throws Exception {
    return Config.mapper.readTree(buf.getBytes());
  }
  
  private JsonNode processTextContent(Buffer buf) throws Exception {
    return Config.mapper.getNodeFactory().textNode(buf.toString());
  }
  
  private JsonNode processBinaryContent(Buffer buf) throws Exception {
    return Config.mapper.getNodeFactory().binaryNode(buf.getBytes());
  }
  
  private HttpClient createHttpClient() {
    return vertx().createHttpClient(new HttpClientOptions()
      .setTryUseCompression(config.compression)
      .setMaxPoolSize(1)
      .setIdleTimeout(config.idleTimeout)
      .setSsl(config.ssl)
      .setDefaultHost(config.host)
      .setDefaultPort(config.port)
      .setKeepAlive(true)
      .setPipelining(false)
      .setMaxWaitQueueSize(-1)
      .setConnectTimeout(config.connectTimeout)
      .setVerifyHost(config.verifyHost)
      .setTrustAll(config.trustAll)
    );
  }
  
  public interface IBufferParser {
    JsonNode parse(Buffer buf) throws Exception;
  }
  
}
