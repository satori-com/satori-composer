package com.satori.mods.core.stats;

public interface IStatsCollector {
  void sum(String aspect, double sum);
  
  void avg(String aspect, double sum, long n);
  
  void norm(String aspect, double sum, long n, double sumSq);
  
  void series(String aspect, double[] values, int offset, int size);
  
  default void sum(String aspect, StatsSum val) {
    if (val != null) {
      sum(aspect, val.sum);
    }
  }
  
  default void avg(String aspect, StatsAvg val) {
    if (val != null) {
      avg(aspect, val.sum, val.n);
    }
  }
  
  default void avg(StatsAvgMetric metric) {
    if (metric != null) {
      avg(metric.aspect, metric.sum, metric.n);
    }
  }
  
  default void norm(String aspect, StatsNorm val) {
    if (val != null) {
      norm(aspect, val.sum, val.n, val.sumSq);
    }
  }
  
  default void norm(StatsNormMetric metric) {
    if (metric != null) {
      norm(metric.aspect, metric.sum, metric.n, metric.sumSq);
    }
  }
  
  default void series(String aspect, StatsSeries val) {
    if (val != null) {
      series(aspect, val.values, 0, val.size);
    }
  }
  
  default void series(StatsSeriesMetric metric) {
    if (metric != null) {
      series(metric.aspect, metric.values, 0, metric.size);
    }
  }
  
  default void rec(IStatsRecord rec) {
    if (rec != null) {
      rec.collect(this);
    }
  }
  
  default void avg(String aspect, double sum) {
    avg(aspect, sum, 1);
  }
  
  default void norm(String aspect, double sum) {
    norm(aspect, sum, 1, sum * sum);
  }
  
  default void series(String aspect, double value) {
    series(aspect, new double[]{value}, 0, 1);
  }
  
  default void series(String aspect, double... values) {
    series(aspect, values, 0, values.length);
  }
  
  default IStatsCollector withPrefix(String name) {
    return new IStatsCollector() {
      
      @Override
      public void sum(String aspect, double sum) {
        IStatsCollector.this.sum(name + "." + aspect, sum);
      }
      
      @Override
      public void avg(String aspect, double sum, long n) {
        IStatsCollector.this.avg(name + "." + aspect, sum, n);
      }
      
      @Override
      public void norm(String aspect, double sum, long n, double sumSq) {
        IStatsCollector.this.norm(name + "." + aspect, sum, n, sumSq);
      }
      
      @Override
      public void series(String aspect, double[] values, int offset, int size) {
        IStatsCollector.this.series(name + "." + aspect, values, offset, size);
      }
    };
  }
}
