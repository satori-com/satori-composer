package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;


/**
 * Identification information for the vehicle performing the trip.
 */
@JsonInclude(Include.NON_NULL)
public class GtfsVehicleDescriptor
  extends GtfsObject {
  
  /**
   * The license plate of the vehicle.
   */
  @JsonProperty("license_plate")
  public String licensePlate;
  /**
   * Internal system identification of the vehicle. Should be unique per
   * vehicle, and can be used for tracking the vehicle as it proceeds through
   * the system.
   */
  @JsonProperty("id")
  public String id;
  /**
   * User visible label, i.e., something that must be shown to the passenger to
   * help identify the correct vehicle.
   */
  @JsonProperty("label")
  public String label;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    GtfsVehicleDescriptor that = ((GtfsVehicleDescriptor) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.licensePlate, that.licensePlate)) {
      return false;
    }
    if (!Objects.equals(this.id, that.id)) {
      return false;
    }
    if (!Objects.equals(this.label, that.label)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = (super.hashCode());
    result = ((result * 31) + (this.licensePlate != null ? this.licensePlate.hashCode() : 0));
    result = ((result * 31) + (this.id != null ? this.id.hashCode() : 0));
    result = ((result * 31) + (this.label != null ? this.label.hashCode() : 0));
    return result;
  }
  
}
