package com.satori.composer.rtm;

import com.satori.composer.rtm.core.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import io.vertx.core.*;
import io.vertx.core.http.*;


public abstract class RtmChannelSubscriber extends RtmChannel implements IRtmSubscriberContext {
  private RtmSubscriber subscriber = null;
  private final String channel;
  private final String filter;
  private final Map history;
  
  public RtmChannelSubscriber(Vertx vertx, RtmDriverConfig config, String name) {
    super(vertx, config, name);
    channel = config.channel;
    filter = config.filter;
    history = config.history;
  }
  
  @Override
  protected IRtmState<Void> createConnectedState(WebSocket ws) {
    return new RtmConnectedState<RtmChannelSubscriber>(this, ws) {
      @Override
      protected void onCompletion() {
        subscriber = null;
        super.onCompletion();
      }
      
      @Override
      protected IRtmPipelineHandler createPipeline(WebSocket ws, IRtmPduHandler handler) {
        RtmChannelSubscriber ctx = RtmChannelSubscriber.this;
        subscriber = new RtmSubscriber(ctx, handler);
        publisher = new RtmPublisher(ctx, subscriber);
        final RtmParser parser;
        if (ctx.auth() != null) {
          final RtmAuthenticator auth = new RtmAuthenticator(ctx, publisher);
          parser = new RtmParser(ctx, auth);
        } else {
          parser = new RtmParser(ctx, subscriber);
        }
        final WsPinger pinger = new WsPinger(ctx, parser);
  
        return new WebSockAdapter(ws, ctx, pinger);
      }
    };
  }
  
  @Override
  public String channel() {
    return channel;
  }
  
  @Override
  public String filter() {
    return filter;
  }
  
  @Override
  public Map history() {
    return history;
  }
  
  @Override
  public void onChannelData(JsonNode msg) {
  }
  
  @Override
  public void onSubscribed(String next) {
  }
  
  @Override
  public void onUnsubscribed(String next) {
  }
  
  public void subscribe() {
    if (subscriber != null) {
      subscriber.subscribe();
    }
  }
  
  public void unsubscribe() {
    if (subscriber != null) {
      subscriber.unsubscribe();
    }
  }
  
  
}
