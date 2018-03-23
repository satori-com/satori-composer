package com.satori.composer.config;

import com.satori.mods.core.config.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import io.netty.handler.codec.http.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpRequestConfig extends HttpConnectionConfig {
  public static final int INVALID_PORT = -1;
  
  @JsonProperty("path")
  public String path = null;
  
  @JsonProperty("args")
  public HashMap<String, String> args = null;
  
  @JsonProperty("headers")
  public HashMap<String, String> headers = null;
  
  public HttpRequestConfig() {
  }
  
  public HttpRequestConfig(String host) {
    this.host = host;
  }
  
  public HttpRequestConfig(HttpRequestConfig cfg) {
    super(cfg);
    this.path = cfg.path;
    this.args = cfg.args;
    this.headers = cfg.headers;
  }
  
  public String getUrl(String schema){
    StringBuilder sb = new StringBuilder();
    sb.append(schema);
    if(ssl) {
      sb.append("s://");
    }else {
      sb.append("://");
    }
    sb.append(host);
    if(ssl && port != 443 || !ssl && port != 80){
      sb.append(":");
      sb.append(port);
    }
    if(path!=null){
      // skip unnecessary leading '/'
      for (int i=0; i<path.length(); i++){
        if(path.charAt(i)!='/'){
          sb.append("/");
          sb.append(path, i, path.length());
          break;
        }
      }
    }
    QueryStringEncoder qenc = new QueryStringEncoder("");
    for (HashMap.Entry<String, String> e : args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    sb.append(qenc.toString());
    return sb.toString();
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    super.validate();
    if (args == null) {
      args = new HashMap<>();
    }
    if (headers == null) {
      headers = new HashMap<>();
    }
    if (path == null || path.isEmpty()) {
      path = "/";
    }
  }
  
  public String getPath(String subPath) {
    final String path;
    if (subPath == null || subPath.isEmpty()) {
      path = this.path;
    } else if (this.path == null || this.path.isEmpty()) {
      path = subPath;
    } else {
      if (this.path.endsWith("/") || subPath.startsWith("/")) {
        path = this.path + subPath;
      } else {
        path = this.path + "/" + subPath;
      }
    }
    if (args == null) {
      return path;
    }
    
    QueryStringEncoder qenc = new QueryStringEncoder(path);
    for (HashMap.Entry<String, String> e : args.entrySet()) {
      qenc.addParam(e.getKey(), e.getValue());
    }
    return qenc.toString();
  }
  
  public String getPath() {
    return getPath(null);
  }
}
