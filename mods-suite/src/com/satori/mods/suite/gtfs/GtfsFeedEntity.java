package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;


/**
 * A definition (or update) of an entity in the transit feed.
 */
@JsonInclude(Include.NON_NULL)
public class GtfsFeedEntity
  extends GtfsObject {
  
  /**
   * Whether this entity is to be deleted. Relevant only for incremental
   * fetches.
   */
  @JsonProperty("is_deleted")
  public Boolean isDeleted;
  @JsonProperty("alert")
  public GtfsAlert alert;
  /**
   * Data about the entity itself. Exactly one of the following fields must be
   * present (unless the entity is being deleted).
   */
  @JsonProperty("trip_update")
  public GtfsTripUpdate tripUpdate;
  /**
   * The ids are used only to provide incrementality support. The id should be
   * unique within a FeedMessage. Consequent FeedMessages may contain
   * FeedEntities with the same id. In case of a DIFFERENTIAL update the new
   * FeedEntity with some id will replace the old FeedEntity with the same id
   * (or delete it - see is_deleted below).
   * The actual GTFS entities (e.g. stations, routes, trips) referenced by the
   * feed must be specified by explicit selectors (see EntitySelector below for
   * more info).
   */
  @JsonProperty("id")
  public String id;
  @JsonProperty("vehicle")
  public GtfsVehiclePosition vehicle;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    GtfsFeedEntity that = ((GtfsFeedEntity) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.isDeleted, that.isDeleted)) {
      return false;
    }
    if (!Objects.equals(this.alert, that.alert)) {
      return false;
    }
    if (!Objects.equals(this.tripUpdate, that.tripUpdate)) {
      return false;
    }
    if (!Objects.equals(this.id, that.id)) {
      return false;
    }
    if (!Objects.equals(this.vehicle, that.vehicle)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = (super.hashCode());
    result = ((result * 31) + (this.isDeleted != null ? this.isDeleted.hashCode() : 0));
    result = ((result * 31) + (this.alert != null ? this.alert.hashCode() : 0));
    result = ((result * 31) + (this.tripUpdate != null ? this.tripUpdate.hashCode() : 0));
    result = ((result * 31) + (this.id != null ? this.id.hashCode() : 0));
    result = ((result * 31) + (this.vehicle != null ? this.vehicle.hashCode() : 0));
    return result;
  }
  
}
