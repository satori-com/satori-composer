package com.satori.composer.logs;

import com.satori.composer.runtime.*;

import com.fasterxml.jackson.annotation.*;
import org.slf4j.event.*;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LogRtmRecord extends JsonExt {
  public static final long INVALID_TIMESTAMP = Long.MIN_VALUE;
  
  public static final String LEVEL_ERROR = "ERROR";
  public static final String LEVEL_WARN = "WARN";
  public static final String LEVEL_INFO = "INFO";
  public static final String LEVEL_DEBUG = "DEBUG";
  
  @JsonProperty("message")
  public String message = null;
  
  @JsonProperty("args")
  public String[] args = null;
  
  @JsonProperty("source")
  public String source = null;
  
  @JsonProperty("level")
  public String level = null;
  
  @JsonProperty("timestamp")
  public long timestamp = INVALID_TIMESTAMP; // in ms.
  
  @JsonProperty("thread")
  public String thread = null;
  
  @JsonProperty("error")
  public LogRtmError error = null;
  
  @Override
  public String toString() {
    return String.format(
      "%s [%s] %s - %s",
      thread, level, source, message
    );
  }
  
  public static LogRtmRecord from(LoggingEvent event){
    LogRtmRecord rec = new LogRtmRecord();
    rec.source = event.getLoggerName();
    rec.level = event.getLevel().toString();
    try{
      rec.message = event.getMessage();
    }catch (Exception t){
      rec.message = event.getMessage();
    }
  
    rec.timestamp = event.getTimeStamp();
    rec.thread = event.getThreadName();
    Throwable throwable = event.getThrowable();
    if(throwable != null){
      LogRtmError err = new LogRtmError();
      err.message = throwable.getMessage();
      err.type = throwable.getClass().getCanonicalName();
      StackTraceElement[] stackTrace = throwable.getStackTrace();
      if(stackTrace != null && stackTrace.length >0){
        String[] stack = new String[stackTrace.length];
        for(int j=0; j< stackTrace.length; j+=1){
          stack[j]=stackTrace[j].toString();
        }
        err.stack = stack;
      }
      rec.error = err;
    }
    return rec;
  }
}
