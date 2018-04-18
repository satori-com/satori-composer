package com.satori.composer.rtm.core;

import com.satori.composer.runtime.*;
import com.satori.libs.async.core.*;

import io.vertx.core.http.*;
import org.slf4j.*;
import org.slf4j.helpers.*;


public class WebSockAdapter implements IWsFrameController, IRtmPipelineHandler {
  public static final int defaultClosingTimeout = 2000;
  
  protected final int closingTimeout = defaultClosingTimeout;
  protected final IRtmContext ctx;
  IWsFrameHandler slave;
  IRtmPipelineController master;
  WebSocket ws;
  Stage stage;
  long stageModified;
  
  
  public WebSockAdapter(WebSocket ws, IRtmContext ctx, IWsFrameHandler slave) {
    this.slave = slave;
    this.ws = ws;
    this.ctx = ctx;
    this.stage = Stage.created;
    this.stageModified = Stopwatch.timestamp();
    setupWsHandlers();
  }
  
  private void setupWsHandlers() {
    try {
      ws.frameHandler(this::onWsRecv);
      ws.endHandler(this::onWsEnd);
      ws.exceptionHandler(this::onWsError);
      //ws.drainHandler(this::onWsDrain);
      return;
    } catch (Throwable cause) {
      log().error("failed to setup websocket handlers", cause);
    }
    closeSilently();
  }
  
  private void cleanupWsHandlers() {
    try {
      ws.frameHandler(null);
    } catch (Throwable cause) {
      log().warn("exception when cleaning up 'frame' handler", cause);
    }
    try {
      ws.endHandler(null);
    } catch (Throwable cause) {
      log().warn("exception when cleaning up 'end' handler", cause);
    }
    try {
      ws.exceptionHandler(null);
    } catch (Throwable cause) {
      log().warn("exception when cleaning up 'exception' handler", cause);
    }
  }
  
  private Stage closeSilently() {
    Stage prev = stage(Stage.closed);
    if (ws != null) {
      cleanupWsHandlers();
      try {
        ws.close();
      } catch (Throwable cause) {
        log().error("failed to close websocket", cause);
      }
      ws = null;
    }
    return prev;
  }
  
  // IRtmPduHandler implementation
  
  @Override
  public void onStart(IRtmPipelineController master) {
    if (stage != Stage.created) {
      master.fail(new CheckFailedException("stage({}) != created", stage));
      return;
    }
    try {
      this.master = master;
      stage(Stage.started);
      slave.onStart(this);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  @Override
  public void onStop() {
    Stage prev = closeSilently();
    if (prev == Stage.started || prev == Stage.closing) {
      try {
        slave.onStop();
      } catch (Throwable cause) {
        log().error("unhandled exception in onStop", cause);
      }
    }
  }
  
  @Override
  public void onPulse(long timestamp) {
    
    // do not propagate pulses if not in started or closing stages
    if (stage != Stage.closing && stage != Stage.started) {
      log().info("pulse ignored");
      return;
    }
    
    try {
      slave.onPulse(timestamp);
    } catch (Throwable cause) {
      log().error("unexpected exception during pulse", cause);
    }
    
    // check if we in closing stage for too long
    if (stage == Stage.closing) {
      if (timestamp - stageModified > closingTimeout) {
        fail("time out");
      }
    }
  }
  
  // IWsFrameController implementation
  
  @Override
  public void send(WebSocketFrame frame) {
    if (!checkStarted()) {
      return;
    }
    if (ws == null) {
      log().error("check 'not in closing stage' failed");
      return;
    }
    try {
      ws.writeFrame(frame);
    } catch (Throwable cause) {
      fail(cause);
    }
    if (!isWritable()) {
      ws.drainHandler(this::onWsDrain);
    }
  }
  
  @Override
  public boolean isWritable() {
    return !ws.writeQueueFull();
  }
  
  @Override
  public void fail(Throwable cause) {
    master.fail(cause);
    //log().warn("failure fired", cause);
    //Stage prev = closeSilently();
        /*if(prev == Stage.started || prev == Stage.closing){
            try {
                slave.onStop();
            }catch (Throwable ex){
                log().error("unhandled exception during onStop", ex);
            }
        }*/
    
  }
  
  @Override
  public void close() {
    switch (stage) {
      case started:
        stage(Stage.closing);
        try {
          ws.close();
        } catch (Throwable cause) {
          fail(cause);
        }
        return;
      case closing:
      case closed:
        //already in clos(ing/ed) stage
        log().warn("already in {} stage", stage);
        return;
      default:
        fail("unexpected stage ({}) during close", stage);
        return;
    }
  }
  
  // fail helper methods
  
  public void fail(String message) {
    fail(new Exception(message));
  }
  
  public void fail(String message, Object... args) {
    fail(new Exception(
      MessageFormatter.format(message, args).getMessage()
    ));
  }
  
  // websocket handlers
  
  public void onWsRecv(WebSocketFrame frame) {
    if (stage != Stage.started && stage != Stage.closing) {
      fail("unexpected frame received");
      return;
    }
    try {
      slave.onRecv(frame);
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  public void onWsDrain(Void unused) {
    if (stage != Stage.started && stage != Stage.closing) {
      fail("unexpected callback");
      return;
    }
    try {
      slave.onWritableChanged(isWritable());
    } catch (Throwable cause) {
      fail(cause);
    }
  }
  
  public void onWsEnd(Void nothing) {
    Stage prev = stage(Stage.closed);
    if (ws != null) {
      cleanupWsHandlers();
      ws = null;
    }
    switch (prev) {
      case started:
        log().warn("connection unexpectedly closed");
      case closing:
        try {
          slave.onStop();
        } catch (Throwable cause) {
          log().error("unhandled exception", cause);
        }
        try {
          master.close();
        } catch (Throwable cause) {
          log().error("unhandled exception", cause);
        }
        return;
      default:
        log().error("unexpected stage ({}) when websocket closed", stage);
    }
  }
  
  public void onWsError(Throwable cause) {
    log().error("websocket error", cause);
    fail(cause);
  }
  
  // stage checking methods
  
  public void checkFailed(String message, Object... args) {
    String text = MessageFormatter.format(message, args).getMessage();
    log().error(text, new Exception(text));
  }
  
  public boolean checkNotClosing() {
    if (stage == Stage.closing) {
      checkFailed("check 'not in closing stage' failed");
      return false;
    }
    return true;
  }
  
  public boolean checkNotClosed() {
    if (stage == Stage.closed) {
      checkFailed("check 'not in closed stage' failed");
      return false;
    }
    return true;
  }
  
  public boolean checkStarted() {
    if (stage != Stage.started) {
      checkFailed("check 'in started stage' failed ({})", stage);
      return false;
    }
    return true;
  }
  
  public boolean checkCreated() {
    if (stage != Stage.created) {
      checkFailed("check 'in created stage' failed ({})", stage);
      return false;
    }
    return true;
  }
  
  // other functional
  
  public Logger log() {
    return ctx.log();
  }
  
  private Stage stage(Stage val) {
    Stage prev = stage;
    stage = val;
    if (prev != stage) {
      stageModified = Stopwatch.timestamp();
    }
    return prev;
  }
  
  
  public enum Stage {
    created, started, closing, closed
  }
  
}