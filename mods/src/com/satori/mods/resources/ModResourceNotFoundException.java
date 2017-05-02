package com.satori.mods.resources;

public class ModResourceNotFoundException extends Exception {
  public final String resourceName;
  
  public ModResourceNotFoundException(String resourceName) {
    super("resource '" + resourceName + "' not found");
    this.resourceName = resourceName;
  }
}
