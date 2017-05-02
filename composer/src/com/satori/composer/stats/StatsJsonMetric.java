package com.satori.composer.stats;

import com.satori.mods.core.stats.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StatsJsonMetric extends StatsJsonExt {
  public static final String SUM_TYPE = "sum";
  public static final String NORM_TYPE = "norm";
  public static final String AVG_TYPE = "avg";
  public static final String SERIES_TYPE = "series";
  
  @JsonProperty("aspect")
  public String aspect = null;
  
  @JsonProperty("type")
  public String type = null;
  
  @JsonProperty("sum")
  public double sum = Double.NaN;
  
  @JsonProperty("n")
  public long n = 0;
  
  @JsonProperty("sum-sq")
  public double sumSq = Double.NaN;
  
  @JsonProperty("series")
  public double[] series = null;
  
  public StatsJsonMetric() {
  }
  
  public static StatsJsonMetric series(String aspect, double[] values, int offset, int size) {
    StatsJsonMetric res = new StatsJsonMetric();
    res.type = SERIES_TYPE;
    res.aspect = aspect;
    res.series = new double[size];
    System.arraycopy(values, offset, res.series, 0, size);
    return res;
  }
  
  public static StatsJsonMetric series(StatsSeriesMetric metric) {
    return series(metric.aspect, metric.values, 0, metric.size);
  }
  
  public static StatsJsonMetric sum(String aspect, double sum) {
    StatsJsonMetric res = new StatsJsonMetric();
    res.type = SUM_TYPE;
    res.aspect = aspect;
    res.sum = sum;
    return res;
  }
  
  public static StatsJsonMetric sum(StatsSumMetric metric) {
    return sum(metric.aspect, metric.sum);
  }
  
  public static StatsJsonMetric avg(String aspect, double sum, long n) {
    StatsJsonMetric res = new StatsJsonMetric();
    res.type = AVG_TYPE;
    res.aspect = aspect;
    res.sum = sum;
    res.n = n;
    return res;
  }
  
  public static StatsJsonMetric avg(StatsAvgMetric metric) {
    return avg(metric.aspect, metric.sum, metric.n);
  }
  
  public static StatsJsonMetric norm(String aspect, double sum, long n, double sumSq) {
    StatsJsonMetric res = new StatsJsonMetric();
    res.type = NORM_TYPE;
    res.aspect = aspect;
    res.sum = sum;
    res.n = n;
    res.sumSq = sumSq;
    return res;
  }
  
  public static StatsJsonMetric norm(StatsNormMetric metric) {
    return norm(metric.aspect, metric.sum, metric.n, metric.sumSq);
  }
}
