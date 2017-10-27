package com.satori.composer.rtm.core;


import com.satori.libs.async.api.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;

public class RtmPublisher extends RtmPduInterceptor<IRtmContext> {
  
  private HashMap<String, IAsyncHandler<String>> awaiters;
  
  public RtmPublisher(IRtmContext ctx, IRtmPduHandler slave) {
    super(ctx, slave);
    this.awaiters = null;
  }
  
  // IRtmPduHandler implementation
  
  @Override
  public void onStart(IRtmPduController master) throws Throwable {
    this.master = master;
    awaiters = new HashMap<>();
    slave.onStart(this);
  }
  
  @Override
  public void onStop() throws Throwable {
    master = null;
    if (this.awaiters != null) {
      HashMap<String, IAsyncHandler<String>> awaiters = this.awaiters;
      this.awaiters = null;
      
      for (IAsyncHandler cont : awaiters.values()) {
        try {
          cont.fail(new Exception("operation aborted"));
        } catch (Throwable cause) {
          // TODO: rethrow as aggregated exception after all
          log().warn("unhandled exception", cause);
        }
      }
    }
    
    slave.onStop();
  }
  
  @Override
  public void onRecv(RtmPdu<JsonNode> pdu) throws Throwable {
    do {
      if (awaiters == null) {
        break;
      }
      String id = pdu.id;
      if (id == null || id.isEmpty()) {
        break;
      }
      IAsyncHandler<String> cont = awaiters.remove(pdu.id);
      if (cont == null) {
        break;
      }
      
      if (pdu.action.equals("rtm/publish/ok")) {
        String next = null;
        try {
          next = pdu.body.get("position").asText();
        } catch (Exception cause) {
          log().warn("unexpected payload", cause);
        }
        
        try {
          cont.succeed(next);
        } catch (Throwable e) {
          log().warn("unhandled exception", e);
        }
        return;
      }
      
      try {
        cont.fail(new Exception(String.format(
          "publish error: %s", pdu.body
        )));
      } catch (Throwable e) {
        log().warn("unhandled exception", e);
      }
      return;
      
    } while (false);
    slave.onRecv(pdu);
  }
  
  @Override
  public void fail(Throwable cause) {
    master.fail(cause);
  }
  
  // IPulseObject implementation
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    slave.onPulse(timestamp);
  }
  
  public <T> String publish(String channel, T msg, IAsyncHandler<String> cont) {
    if (awaiters == null) {
      if (cont != null) {
        cont.fail(new Exception("not started"));
      }
      return null;
    }
    
    String id = genUid();
    if (cont != null) {
      cont = awaiters.put(id, cont);
      if (cont != null) {
        cont.fail(new Exception("duplicated id"));
        fail(new Exception("duplicated id"));
      }
    }
    
    master.send(new RtmPublishPdu<T>(channel, msg, id));
    return id;
  }
}