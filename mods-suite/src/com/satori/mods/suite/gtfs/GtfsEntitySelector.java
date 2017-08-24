package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;


/**
 * A selector for an entity in a GTFS feed.
 */
@JsonInclude(Include.NON_NULL)
public class GtfsEntitySelector
  extends GtfsObject {
  
  @JsonProperty("route_id")
  public String routeId;
  @JsonProperty("trip")
  public GtfsTripDescriptor trip;
  /**
   * corresponds to route_type in GTFS.
   */
  @JsonProperty("route_type")
  public Integer routeType;
  @JsonProperty("stop_id")
  public String stopId;
  /**
   * The values of the fields should correspond to the appropriate fields in the
   * GTFS feed.
   * At least one specifier must be given. If several are given, then the
   * matching has to apply to all the given specifiers.
   */
  @JsonProperty("agency_id")
  public String agencyId;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    GtfsEntitySelector that = ((GtfsEntitySelector) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.routeId, that.routeId)) {
      return false;
    }
    if (!Objects.equals(this.trip, that.trip)) {
      return false;
    }
    if (!Objects.equals(this.routeType, that.routeType)) {
      return false;
    }
    if (!Objects.equals(this.stopId, that.stopId)) {
      return false;
    }
    if (!Objects.equals(this.agencyId, that.agencyId)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = (super.hashCode());
    result = ((result * 31) + (this.routeId != null ? this.routeId.hashCode() : 0));
    result = ((result * 31) + (this.trip != null ? this.trip.hashCode() : 0));
    result = ((result * 31) + (this.routeType != null ? this.routeType.hashCode() : 0));
    result = ((result * 31) + (this.stopId != null ? this.stopId.hashCode() : 0));
    result = ((result * 31) + (this.agencyId != null ? this.agencyId.hashCode() : 0));
    return result;
  }
  
}
