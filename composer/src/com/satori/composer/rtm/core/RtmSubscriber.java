package com.satori.composer.rtm.core;


import com.satori.composer.runtime.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class RtmSubscriber extends RtmPduInterceptor<IRtmSubscriberContext> {
  
  //private long timestamp;
  private IPduProcessor pduProcessor;
  //private boolean subscribed;
  
  public RtmSubscriber(IRtmSubscriberContext ctx, IRtmPduHandler slave) {
    super(ctx, slave);
    this.pduProcessor = null;
    //this.subscribed = false;
  }
  
  // IRtmPduHandler implementation
  
  @Override
  public void onStart(IRtmPduController master) throws Throwable {
    this.master = master;
    //timestamp = Stopwatch.timestamp();
    enterSubscribingState();
    slave.onStart(this);
  }
  
  @Override
  public void onStop() throws Throwable {
    master = null;
    pduProcessor = null;
    //subscribed = false;
    slave.onStop();
  }
  
  @Override
  public void onRecv(RtmPdu<JsonNode> pdu) throws Throwable {
    if (pduProcessor == null || !pduProcessor.process(pdu)) {
      slave.onRecv(pdu);
    }
  }
  
  @Override
  public void fail(Throwable cause) {
    master.fail(cause);
  }
  
  // IPulseObject implementation
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    
    if (pduProcessor == null) {
      return;
    }
    pduProcessor.pulse(timestamp);
    // TODO: fix the case when pduProcessor.pulse triggers completion e.g. fail
    if (slave != null) {
      slave.onPulse(timestamp);
    }
  }
  
  // extra
  
  public boolean subscribe() {
    return pduProcessor.subscribe();
  }
  
  public boolean unsubscribe() {
    return pduProcessor.subscribe();
  }
  
  // subscribing state
  
  protected void enterSubscribingState() {
    log().info("rtm subscribing... ({})", ctx);
    try {
      //subscribed = false;
      String id = UUID.randomUUID().toString();
      pduProcessor = new IPduProcessor() {
        boolean unsubscribe = false;
        long timeout = Stopwatch.timestamp() + ctx.subscribeTimeout();
        
        @Override
        public boolean process(RtmPdu<JsonNode> pdu) throws Throwable {
          return onSubscribeReply(pdu, id, unsubscribe);
        }
        
        @Override
        public boolean subscribe() {
          if (!unsubscribe) {
            log().info("already in subscribe mode ({})", ctx);
            return false;
          }
          unsubscribe = false;
          return true;
        }
        
        @Override
        public boolean unsubscribe() {
          if (unsubscribe) {
            log().info("already in unsubscribe mode ({})", ctx);
            return false;
          }
          unsubscribe = true;
          return false;
        }
        
        @Override
        public void pulse(long ts) {
          if (ts > timeout) {
            fail(new Exception("subscribe timeout"));
          }
        }
      };
      RtmSubscribePdu pdu = new RtmSubscribePdu(channel(), filter(), id, null, history());
      master.send(pdu);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  protected boolean onSubscribeReply(RtmPdu<JsonNode> pdu, String id, boolean unsubscribe) {
    try {
      if (!id.equals(pdu.id)) {
        return false;
      }
      
      if (pdu.action.equals("rtm/subscribe/ok")) {
        enterSubscribedState(
          pdu.body != null ? pdu.body.get("position").asText() : null, unsubscribe
        );
        return true;
      } else if (pdu.action.equals("rtm/subscribe/error")) {
        fail(new Exception(String.format(
          "subscribe error: %s", pdu.body
        )));
        return true;
      }
    } catch (Throwable cause) {
      fail(cause);
      return true;
    }
    
    return false;
  }
  
  // subscribed state
  
  protected void enterSubscribedState(String next, boolean unsubscribe) {
    try {
      log().info("rtm subscribed ({})", ctx);
      //subscribed = true;
      //timestamp = Stopwatch.timestamp();
      pduProcessor = new IPduProcessor() {
        long timeout = Stopwatch.timestamp() + ctx.subscriptionIdleTimeout();
        
        @Override
        public boolean process(RtmPdu<JsonNode> pdu) throws Throwable {
          if (onChannelData(pdu)) {
            timeout = Stopwatch.timestamp() + ctx.subscriptionIdleTimeout();
            return true;
          }
          return false;
        }
        
        @Override
        public boolean subscribe() {
          log().debug("already subscribed ({})", ctx);
          if (unsubscribe) {
            // TODO: FIXIT!!!
            log().error("subscribe ignored!!!! ({})", ctx);
          }
          return false;
        }
        
        @Override
        public boolean unsubscribe() {
          // TODO: fix confusing logic
          if (!unsubscribe) {
            enterUnsubscribingState();
          }
          return true;
        }
        
        @Override
        public void pulse(long ts) {
          if (ts > timeout) {
            fail(new Exception("idle timeout"));
          }
        }
      };
      ctx.onSubscribed(next);
      if (unsubscribe) {
        enterUnsubscribingState();
      }
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  protected boolean onChannelData(RtmPdu<JsonNode> pdu) {
    
    if (pdu.action.equals("rtm/subscription/data")) {
      if (!channel().equals(pdu.body.get("subscription_id").textValue())) {
        return false;
      }
      //timestamp = Stopwatch.timestamp();
      ArrayNode messages = (ArrayNode) pdu.body.get("messages");
      for (JsonNode m : messages) {
        try {
          ctx.onChannelData(m);
        } catch (Throwable cause) {
          log().error("unhandled exception during rtm message processing", cause);
        }
      }
      return true;
    }
    
    if (pdu.action.equals("rtm/subscription/error")) {
      if (!channel().equals(pdu.body.get("subscription_id").textValue())) {
        return false;
      }
      
      //subscribed = false;
      fail(new Exception(String.format(
        "channel error: %s", pdu.body
      )));
      
      return true;
    }
    
    return false;
  }
  
  // unsubscribing state
  
  protected void enterUnsubscribingState() {
    log().info("rtm unsubscribing... ({})", ctx);
    try {
      //subscribed = false;
      String id = UUID.randomUUID().toString();
      pduProcessor = new IPduProcessor() {
        boolean resubscribe = false;
        long timeout = Stopwatch.timestamp() + ctx.subscribeTimeout();
        
        @Override
        public boolean process(RtmPdu<JsonNode> pdu) throws Throwable {
          return onUnsubscribeReply(pdu, id, resubscribe);
        }
        
        @Override
        public boolean subscribe() {
          if (resubscribe) {
            log().info("already in subscribe mode ({})", ctx);
            return false;
          }
          resubscribe = true;
          return true;
        }
        
        @Override
        public boolean unsubscribe() {
          if (!resubscribe) {
            log().info("already in unsubscribe mode ({})", ctx);
            return false;
          }
          resubscribe = false;
          return true;
        }
        
        @Override
        public void pulse(long ts) {
          if (ts > timeout) {
            fail(new Exception("unsubscribe timeout"));
          }
        }
      };
      RtmUnsubscribePdu pdu = new RtmUnsubscribePdu(channel(), id);
      master.send(pdu);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  protected boolean onUnsubscribeReply(RtmPdu<JsonNode> pdu, String id, boolean resubscribe) {
    try {
      if (!id.equals(pdu.id)) {
        return false;
      }
      
      if (pdu.action.equals("rtm/unsubscribe/ok")) {
        enterUnsubscribedState(
          pdu.body != null ? pdu.body.get("position").asText() : (String) null, resubscribe
        );
        return true;
      } else if (pdu.action.equals("rtm/unsubscribe/error")) {
        fail(new Exception(String.format(
          "unsubscribe error: %s", pdu.body
        )));
        return true;
      }
    } catch (Throwable cause) {
      fail(cause);
      return true;
    }
    
    return false;
  }
  
  // unsubscribed state
  
  protected void enterUnsubscribedState(String next, boolean resubscribe) {
    try {
      log().info("rtm unsubscribed ({})", ctx);
      //subscribed = false;
      //timestamp = Stopwatch.timestamp();
      pduProcessor = new IPduProcessor() {
        @Override
        public boolean process(RtmPdu<JsonNode> pdu) throws Throwable {
          return false;
        }
        
        @Override
        public boolean subscribe() {
          // TODO: fix confusing logic
          if (!resubscribe) {
            enterSubscribingState();
          }
          return true;
        }
        
        @Override
        public boolean unsubscribe() {
          log().info("already unsubscribed ({})", ctx);
          if (resubscribe) {
            // TODO: FIXIT!!!
            log().error("unsubscribe ignored!!!! ({})", ctx);
          }
          return false;
        }
      };
      ctx.onUnsubscribed(next);
      if (resubscribe) {
        enterSubscribingState();
      }
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  String channel() {
    return ctx.channel();
  }
  
  String filter() {
    return ctx.filter();
  }
  
  Map history() {
    return this.ctx.history();
  }
  
  interface IPduProcessor {
    boolean process(RtmPdu<JsonNode> pdu) throws Throwable;
    
    boolean subscribe();
    
    boolean unsubscribe();
    
    default void pulse(long ts) {
    }
  }
  
}