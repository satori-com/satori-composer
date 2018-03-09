package com.satori.libs.async.api;

public class DisposedException extends Throwable {
  public DisposedException(){
    super("disposed", null, false, false);
  }
  public static final DisposedException instance = new DisposedException();
}
