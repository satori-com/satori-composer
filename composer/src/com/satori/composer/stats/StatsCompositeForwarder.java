package com.satori.composer.stats;

import java.util.*;

import io.vertx.core.*;

public class StatsCompositeForwarder implements IStatsForwarder, Iterable<IStatsForwarder> {
  private final static ClosedIterator closedIterator = new ClosedIterator();
  
  private ArrayList<IStatsForwarder> forwarders = new ArrayList<>();
  
  public StatsCompositeForwarder() {
  }
  
  // IComposerRuntimeModule implementation
  
  @Override
  public void onPulse(long timestamp) {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.onPulse(timestamp);
    }
  }
  
  @Override
  public void onStart(Vertx vertx) throws Exception {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.onStart(vertx);
    }
  }
  
  @Override
  public void onStop(Vertx vertx) throws Exception {
    Exception errors = null;
    for (IStatsForwarder c : this) {
      assert c != null;
      try {
        c.onStop(vertx);
      } catch (Exception e) {
        if (errors == null) {
          errors = new Exception();
        }
        errors.addSuppressed(e);
      }
    }
    if (errors != null) {
      throw errors;
    }
  }
  
  
  // IStatsCollector implementation
  
  @Override
  public void sum(String aspect, double sum) {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.sum(aspect, sum);
    }
  }
  
  @Override
  public void avg(String aspect, double sum, long n) {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.avg(aspect, sum, n);
    }
  }
  
  @Override
  public void norm(String aspect, double sum, long n, double sumSq) {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.norm(aspect, sum, n, sumSq);
    }
  }
  
  @Override
  public void series(String aspect, double[] values, int offset, int size) {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.series(aspect, values, offset, size);
    }
  }
  
  // IStatsForwarder implementation
  
  @Override
  public void dispose() {
    for (IStatsForwarder c : this) {
      assert c != null;
      c.dispose();
    }
    forwarders = null;
  }
  
  @Override
  public boolean disposed() {
    return forwarders == null;
  }
  
  // Iterable implementation
  
  public Iterator<IStatsForwarder> iterator() {
    if (forwarders == null) {
      return closedIterator;
    }
    return forwarders.iterator();
  }
  
  // public methods
  
  public void add(IStatsForwarder forwarder) {
    if (forwarder == null) {
      return;
    }
    if (forwarders == null) {
      forwarder.dispose();
      return;
    }
    forwarders.add(forwarder);
  }
  
  public void clear() {
    forwarders.clear();
  }
  
  // inner types
  
  private static class ClosedIterator implements Iterator<IStatsForwarder> {
    @Override
    public boolean hasNext() {
      return false;
    }
    
    @Override
    public IStatsForwarder next() {
      return null;
    }
  }
  
  ;
}
