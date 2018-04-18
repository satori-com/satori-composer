package com.satori.composer.rtm.core;

import com.satori.libs.async.core.*;

import java.nio.charset.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import com.fasterxml.jackson.databind.*;

public class RtmAuthenticator extends RtmPduInterceptor<IRtmAuthenticatorContext> {
  
  private long timestamp;
  private IPduProcessor pduProcessor;
  private boolean authenticated;
  
  public RtmAuthenticator(IRtmAuthenticatorContext ctx, IRtmPduHandler slave) {
    super(ctx, slave);
    this.pduProcessor = null;
    this.authenticated = false;
  }
  
  // IRtmPduHandler implementation
  
  @Override
  public void onStart(IRtmPduController master) throws Throwable {
    this.master = master;
    timestamp = Stopwatch.timestamp();
    enterHandshakingState();
  }
  
  @Override
  public void onStop() throws Throwable {
    this.master = null;
    pduProcessor = null;
    if (authenticated) {
      slave.onStop();
    }
  }
  
  @Override
  public void onRecv(RtmPdu<JsonNode> pdu) throws Throwable {
    pduProcessor.process(pdu);
  }
  
  // IPulseObject implementation
  
  @Override
  public void onPulse(long timestamp) throws Throwable {
    if (authenticated) {
      slave.onPulse(timestamp);
    } else {
      long elapsed = timestamp - this.timestamp;
      if (elapsed > ctx.authenticateTimeout()) {
        fail(new Exception("authentication timeout"));
      }
    }
  }
  
  // handshake state
  
  protected void enterHandshakingState() {
    try {
      String id = UUID.randomUUID().toString();
      RtmAuthHandshakePdu pdu = new RtmAuthHandshakePdu(ctx.auth().role, id);
      log().info("rtm authenticating... ({})", ctx);
      master.send(pdu);
      pduProcessor = p -> onHandshakeReply(p, id);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  protected void onHandshakeReply(RtmPdu<JsonNode> pdu, String id) {
    if (!id.equals(pdu.id)) {
      
      fail(new Exception("unexpected pdu received during authentication handshake"));
      
    } else if (pdu.action.equals("auth/handshake/ok")) {
      String nonce = pdu.body.get("data").get("nonce").textValue();
      enterAuthenticatingState(nonce);
      
    } else if (pdu.action.equals("auth/handshake/error")) {
      
      fail(new Exception(String.format(
        "handshake error: %s", pdu.body
      )));
      
    }
  }
  
  // authenticating state
  
  protected void enterAuthenticatingState(String nonce) {
    try {
      String id = UUID.randomUUID().toString();
      String hash = computeHash(ctx.auth().secret, nonce);
      //log().info("nonce: '{}', secret: '{}', hash: '{}' ({})", nonce, ctx.auth().secret, hash, ctx);
      RtmAuthenticatePdu pdu = new RtmAuthenticatePdu(hash, id);
      master.send(pdu);
      pduProcessor = p -> onAuthenticateReply(p, id);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  protected void onAuthenticateReply(RtmPdu<JsonNode> pdu, String id) {
    
    if (!id.equals(pdu.id)) {
      
      fail(new Exception("unexpected pdu received during authentication"));
      
    } else if (pdu.action.equals("auth/authenticate/ok")) {
      log().info("rtm authenticated ({})", ctx);
      pduProcessor = slave::onRecv;
      authenticated = true;
      try {
        slave.onStart(this);
      } catch (Throwable cause) {
        fail(cause);
      }
      
    } else if (pdu.action.equals("auth/authenticate/error")) {
      
      fail(new Exception(String.format(
        "authentication failed: %s", pdu.body
      )));
      
    }
  }
  
  public static String computeHash(String sk, String nonce) throws Exception {
    final String alg = "hmacmd5";
    byte[] skBin = sk.getBytes(StandardCharsets.UTF_8);
    byte[] nonceBin = nonce.getBytes(StandardCharsets.UTF_8);
    SecretKeySpec key = new SecretKeySpec(skBin, alg);
    Mac mac = Mac.getInstance(alg);
    mac.init(key);
    byte[] bytes = mac.doFinal(nonceBin);
    return Base64.getEncoder().encodeToString(bytes);
  }
  
  interface IPduProcessor {
    void process(RtmPdu<JsonNode> pdu) throws Throwable;
  }
}