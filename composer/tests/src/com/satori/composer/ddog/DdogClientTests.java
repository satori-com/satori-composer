package com.satori.composer.ddog;

import com.satori.composer.logs.*;
import com.satori.mods.core.config.*;

import java.util.*;

import io.vertx.core.*;
import org.slf4j.*;
import org.slf4j.event.*;

public class DdogClientTests {
  public static Logger log = LoggerFactory.getLogger(DdogClientTests.class);
  
  public static void main(String... args) throws InvalidConfigException {
    Vertx vertx = Vertx.vertx();
    DdogClientConfig config = new DdogClientConfig();
    config.args = new HashMap<String, String>() {{
      put("api_key", "");
      put("application_key", "");
    }};
    config.validate();
    
    DdogClient client = new DdogClient(vertx, config);
    
    LogRtmMessage msg = new LogRtmMessage();
    msg.prefix = "bot";
    msg.tags = new String[]{
      "project:queenstown", "env:dev", "app:test"
    };
    Exception ex = new Exception("exception message");
    msg.records = new LogRtmRecord[]{
      LogRtmRecord.from(new LoggingEvent() {
        @Override
        public Level getLevel() {
          return Level.ERROR;
        }
        
        @Override
        public Marker getMarker() {
          return null;
        }
        
        @Override
        public String getLoggerName() {
          return log.getName();
        }
        
        @Override
        public String getMessage() {
          return "logger message: {}, {}";
        }
        
        @Override
        public String getThreadName() {
          return Thread.currentThread().getName();
        }
        
        @Override
        public Object[] getArgumentArray() {
          return new Object[0];
        }
        
        @Override
        public long getTimeStamp() {
          return System.currentTimeMillis();
        }
        
        @Override
        public Throwable getThrowable() {
          return ex;
        }
      })
    };
    
    for (LogRtmRecord rec : msg.records) {
      DdogEvent event = new DdogEvent();
      StringBuilder title = new StringBuilder();
      if (msg.prefix != null && !msg.prefix.isEmpty()) {
        title.append(msg.prefix);
        title.append(": ");
      }
      title.append(rec.message);
      event.title = title.toString();
      event.alertType = DdogEvent.ALERT_TYPE_ERROR;
      StringBuilder body = new StringBuilder();
      if (rec.error != null) {
        body.append(rec.error.message);
        if (rec.error.stack != null && rec.error.stack.length > 0) {
          body.append("\n\nstack trace:");
          for (String stackRecord : rec.error.stack) {
            body.append("\n\t");
            body.append(stackRecord);
          }
        }
      }
      if (rec.thread != null) {
        body.append("\n\nthread: ");
        body.append(rec.thread);
      }
      body.append("\n\nNotify: @slack-queenstown-alerts");
      event.text = body.toString();
      event.tags = msg.tags;
      
      client.postEvent(event, ar -> {
        if (!ar.isSucceeded()) {
          log.error("failed to send event", ar.getError());
        } else {
          log.info("event successfully sent");
        }
      });
    }
  }
}
