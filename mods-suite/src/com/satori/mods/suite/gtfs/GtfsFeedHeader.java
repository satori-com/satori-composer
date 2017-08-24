package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;


/**
 * Metadata about a feed, included in feed messages.
 */
@JsonInclude(Include.NON_NULL)
public class GtfsFeedHeader
  extends GtfsObject {
  
  /**
   * Version of the feed specification.
   * The current version is 1.0.
   */
  @JsonProperty("gtfs_realtime_version")
  public String gtfsRealtimeVersion;
  @JsonProperty("incrementality")
  public Incrementality incrementality;
  /**
   * This timestamp identifies the moment when the content of this feed has been
   * created (in server time). In POSIX time (i.e., number of seconds since
   * January 1st 1970 00:00:00 UTC).
   */
  @JsonProperty("timestamp")
  public Long timestamp;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    GtfsFeedHeader that = ((GtfsFeedHeader) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.gtfsRealtimeVersion, that.gtfsRealtimeVersion)) {
      return false;
    }
    if (!Objects.equals(this.incrementality, that.incrementality)) {
      return false;
    }
    if (!Objects.equals(this.timestamp, that.timestamp)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = (super.hashCode());
    result = ((result * 31) + (this.gtfsRealtimeVersion != null ? this.gtfsRealtimeVersion.hashCode() : 0));
    result = ((result * 31) + (this.incrementality != null ? this.incrementality.hashCode() : 0));
    result = ((result * 31) + (this.timestamp != null ? this.timestamp.hashCode() : 0));
    return result;
  }
  
  
  /**
   * Determines whether the current fetch is incremental.  Currently,
   * DIFFERENTIAL mode is unsupported and behavior is unspecified for feeds
   * that use this mode.  There are discussions on the GTFS-realtime mailing
   * list around fully specifying the behavior of DIFFERENTIAL mode and the
   * documentation will be updated when those discussions are finalized.
   */
  public enum Incrementality {
    
    FULL_DATASET(0),
    DIFFERENTIAL(1);
    @JsonValue
    public int value;
    
    Incrementality(int value) {
      this.value = value;
    }
    
    @JsonCreator
    public static GtfsFeedHeader.Incrementality fromInt(int value) {
      switch (value) {
        case 0:
          return GtfsFeedHeader.Incrementality.FULL_DATASET;
        case 1:
          return GtfsFeedHeader.Incrementality.DIFFERENTIAL;
        default:
          return null;
      }
    }
    
  }
  
}
