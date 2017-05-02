package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;


/**
 * A position.
 */
@JsonInclude(Include.NON_NULL)
public class GtfsPosition
  extends GtfsObject {
  
  /**
   * Odometer value, in meters.
   */
  @JsonProperty("odometer")
  public Double odometer;
  /**
   * Bearing, in degrees, clockwise from North, i.e., 0 is North and 90 is East.
   * This can be the compass bearing, or the direction towards the next stop
   * or intermediate location.
   * This should not be direction deduced from the sequence of previous
   * positions, which can be computed from previous data.
   */
  @JsonProperty("bearing")
  public Float bearing;
  /**
   * Degrees North, in the WGS-84 coordinate system.
   */
  @JsonProperty("latitude")
  public Float latitude;
  /**
   * Momentary speed measured by the vehicle, in meters per second.
   */
  @JsonProperty("speed")
  public Float speed;
  /**
   * Degrees East, in the WGS-84 coordinate system.
   */
  @JsonProperty("longitude")
  public Float longitude;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    GtfsPosition that = ((GtfsPosition) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.odometer, that.odometer)) {
      return false;
    }
    if (!Objects.equals(this.bearing, that.bearing)) {
      return false;
    }
    if (!Objects.equals(this.latitude, that.latitude)) {
      return false;
    }
    if (!Objects.equals(this.speed, that.speed)) {
      return false;
    }
    if (!Objects.equals(this.longitude, that.longitude)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = (super.hashCode());
    result = ((result * 31) + (this.odometer != null ? this.odometer.hashCode() : 0));
    result = ((result * 31) + (this.bearing != null ? this.bearing.hashCode() : 0));
    result = ((result * 31) + (this.latitude != null ? this.latitude.hashCode() : 0));
    result = ((result * 31) + (this.speed != null ? this.speed.hashCode() : 0));
    result = ((result * 31) + (this.longitude != null ? this.longitude.hashCode() : 0));
    return result;
  }
  
}
