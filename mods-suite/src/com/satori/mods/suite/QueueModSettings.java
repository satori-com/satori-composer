package com.satori.mods.suite;

import com.satori.mods.core.config.*;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class QueueModSettings extends Config {
  public static final int defaultPauseThreshold = 100;
  public static final int INVALID_THRESHOLD = Integer.MIN_VALUE;
  
  @JsonProperty("pause-threshold")
  public int pauseThreshold = defaultPauseThreshold;
  
  @JsonProperty("resume-threshold")
  public int resumeThreshold = INVALID_THRESHOLD;
  
  public QueueModSettings() {
  }
  
  @Override
  public void validate() throws InvalidConfigException {
    if (pauseThreshold <= 0) {
      throw new InvalidConfigException("invalid pause-threshold");
    }
    if (resumeThreshold < 0) {
      resumeThreshold = defaultResumeThreshold(pauseThreshold);
    }
  }
  
  public static int defaultResumeThreshold(int pauseThreshold) {
    return (pauseThreshold * 7) / 10;
  }
}
