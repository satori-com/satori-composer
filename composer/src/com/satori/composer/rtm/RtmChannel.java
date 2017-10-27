package com.satori.composer.rtm;

import com.satori.composer.rtm.core.*;
import com.satori.libs.async.api.*;

import io.vertx.core.*;
import io.vertx.core.http.*;


public class RtmChannel extends RtmBase implements IRtmContext {
  
  public RtmPublisher publisher;
  
  public RtmChannel(Vertx vertx, RtmBaseConfig config, String name) {
    super(vertx, config);
    this.name = name + ", " + this.name;
  }
  
  @Override
  protected IRtmState<Void> createConnectedState(WebSocket ws) {
    return new RtmConnectedState<RtmChannel>(this, ws) {
      @Override
      protected IRtmPipelineHandler createPipeline(WebSocket ws, IRtmPduHandler handler) {
        RtmChannel ctx = RtmChannel.this;
        publisher = new RtmPublisher(ctx, handler);
        final RtmParser parser;
        if (ctx.auth() != null) {
          final RtmAuthenticator auth = new RtmAuthenticator(ctx, publisher);
          parser = new RtmParser(ctx, auth);
        } else {
          parser = new RtmParser(ctx, publisher);
        }
        final WsPinger pinger = new WsPinger(ctx, parser);
        final WebSockAdapter adapter = new WebSockAdapter(ws, ctx, pinger);
        
        return adapter;
      }
    };
  }
  
  @Override
  protected void leaveConnectedState(IRtmState<Void> state) {
    publisher = null;
    super.leaveConnectedState(state);
  }
  
  
  public <T> String publish(String channel, T msg, IAsyncHandler<String> cont) {
    if (publisher == null) {
      if (cont != null) {
        cont.fail(new Exception("not connected"));
      }
      return null;
    }
    return publisher.publish(channel, msg, cont);
  }
  
}
