package com.satori.composer.rtm.core;


import com.satori.mods.core.async.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.*;
import com.fasterxml.jackson.module.afterburner.*;
import io.vertx.core.*;
import io.vertx.core.http.*;
import org.slf4j.*;

public class RtmDefaultContext implements
  IRtmContext,
  IRtmParserContext,
  IRtmAuthenticatorContext,
  IRtmFactoryContext,
  IWsPingerContext {
  
  protected final Vertx vertx;
  protected final Logger log;
  protected final HttpClient http;
  protected final CaseInsensitiveHeaders headers;
  protected final String host;
  protected final String path;
  protected final boolean ssl;
  protected final int port;
  protected final RtmCredentials credentials;
  protected final ObjectMapper mapper;
  protected final long reconnectDelay;
  protected int nextId = 0;
  
  protected final ArrayDeque<IAsyncHandler<Boolean>> onConnected;
  protected final ArrayDeque<IAsyncHandler<Boolean>> onWritable;
  
  
  protected String name;
  
  public RtmDefaultContext(Vertx vertx, RtmBaseConfig cfg) {
    this.vertx = vertx;
    this.log = LoggerFactory.getLogger(this.getClass());
    this.mapper = new ObjectMapper()
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new AfterburnerModule())
      .registerModule(new Jdk8Module());
    
    this.host = cfg.host;
    this.path = cfg.getPath();
    this.port = cfg.port;
    this.ssl = cfg.ssl;
    this.reconnectDelay = cfg.reconnectDelay;
    this.credentials = cfg.auth != null ? new RtmCredentials(cfg.auth) : null;
    
    this.headers = new CaseInsensitiveHeaders();
    if (cfg.headers != null) {
      for (HashMap.Entry<String, String> e : cfg.headers.entrySet()) {
        this.headers.add(e.getKey(), e.getValue());
      }
    }
    
    name = (cfg.ssl ? "wss://" : "ws://") + cfg.host
      + ((cfg.ssl && cfg.port != 443 || !cfg.ssl && cfg.port != 80) ? (":" + Integer.toString(cfg.port)) : "")
      + (this.path);
    
    this.http = createHttp();
    
    this.onConnected = new ArrayDeque<>();
    this.onWritable = new ArrayDeque<>();
  }
  
  //  IRtmContext
  
  @Override
  public Logger log() {
    return log;
  }
  
  @Override
  public String genUid() {
    return Integer.toString(nextId++);
  }
  
  @Override
  public ArrayDeque<IAsyncHandler<Boolean>> onConnected() {
    return onConnected;
  }
  
  @Override
  public ArrayDeque<IAsyncHandler<Boolean>> onWritable() {
    return onWritable;
  }
  
  //  IRtmFactoryContext
  
  @Override
  public CaseInsensitiveHeaders headers() {
    return headers;
  }
  
  @Override
  public String host() {
    return host;
  }
  
  @Override
  public String path() {
    return path;
  }
  
  @Override
  public int port() {
    return port;
  }
  
  @Override
  public boolean ssl() {
    return ssl;
  }
  
  @Override
  public long reconnectDelay() {
    return reconnectDelay;
  }
  
  @Override
  public void connect(IAsyncHandler<WebSocket> cont) {
    http.websocket(
      path, headers, cont::succeed, cont::fail
    );
  }
  
  
  //  IRtmParserContext
  
  @Override
  public ObjectMapper mapper() {
    return mapper;
  }
  
  // IRtmAuthenticatorContext
  
  @Override
  public RtmCredentials auth() {
    return credentials;
  }
  
  HttpClient createHttp() {
    return vertx.createHttpClient(new HttpClientOptions()
      .setDefaultHost(host())
      .setDefaultPort(port())
      .setTcpKeepAlive(false)
      .setMaxWaitQueueSize(0)
      .setMaxPoolSize(Integer.MAX_VALUE)
      .setSsl(ssl())
      .setIdleTimeout(idleTimeout())
      .setConnectTimeout(connectTimeout())
      .setMaxWebsocketFrameSize(maxFrameSize())
      .setVerifyHost(false)
      .setTrustAll(true)
    );
  }
  
  public String name() {
    return name;
  }
  
  @Override
  public String toString() {
    return name;
  }
}