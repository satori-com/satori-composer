package com.satori.mods.suite.gtfs;

import java.util.*;

import com.fasterxml.jackson.annotation.*;


/**
 * Entities used in the feed.
 * <p>
 * Realtime update of the progress of a vehicle along a trip.
 * Depending on the value of ScheduleRelationship, a TripUpdate can specify:
 * - A trip that proceeds along the schedule.
 * - A trip that proceeds along a route but has no fixed schedule.
 * - A trip that have been added or removed with regard to schedule.
 * <p>
 * The updates can be for future, predicted arrival/departure events, or for
 * past events that already occurred.
 * Normally, updates should get more precise and more certain (see
 * uncertainty below) as the events gets closer to current time.
 * Even if that is not possible, the information for past events should be
 * precise and certain. In particular, if an update points to time in the past
 * but its update's uncertainty is not 0, the client should conclude that the
 * update is a (wrong) prediction and that the trip has not completed yet.
 * <p>
 * Note that the update can describe a trip that is already completed.
 * To this end, it is enough to provide an update for the last stop of the trip.
 * If the time of that is in the past, the client will conclude from that that
 * the whole trip is in the past (it is possible, although inconsequential, to
 * also provide updates for preceding stops).
 * This option is most relevant for a trip that has completed ahead of schedule,
 * but according to the schedule, the trip is still proceeding at the current
 * time. Removing the updates for this trip could make the client assume
 * that the trip is still proceeding.
 * Note that the feed provider is allowed, but not required, to purge past
 * updates - this is one case where this would be practically useful.
 */
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class GtfsTripUpdate
  extends GtfsObject {
  
  /**
   * The Trip that this message applies to. There can be at most one
   * TripUpdate entity for each actual trip instance.
   * If there is none, that means there is no prediction information available.
   * It does *not* mean that the trip is progressing according to schedule.
   */
  @JsonProperty("trip")
  public GtfsTripDescriptor trip;
  /**
   * The current schedule deviation for the trip.  Delay should only be
   * specified when the prediction is given relative to some existing schedule
   * in GTFS.
   * <p>
   * Delay (in seconds) can be positive (meaning that the vehicle is late) or
   * negative (meaning that the vehicle is ahead of schedule). Delay of 0
   * means that the vehicle is exactly on time.
   * <p>
   * Delay information in StopTimeUpdates take precedent of trip-level delay
   * information, such that trip-level delay is only propagated until the next
   * stop along the trip with a StopTimeUpdate delay value specified.
   * <p>
   * Feed providers are strongly encouraged to provide a TripUpdate.timestamp
   * value indicating when the delay value was last updated, in order to
   * evaluate the freshness of the data.
   * <p>
   * NOTE: This field is still experimental, and subject to change. It may be
   * formally adopted in the future.
   */
  @JsonProperty("delay")
  public Integer delay;
  /**
   * Updates to StopTimes for the trip (both future, i.e., predictions, and in
   * some cases, past ones, i.e., those that already happened).
   * The updates must be sorted by stop_sequence, and apply for all the
   * following stops of the trip up to the next specified one.
   * <p>
   * Example 1:
   * For a trip with 20 stops, a StopTimeUpdate with arrival delay and departure
   * delay of 0 for stop_sequence of the current stop means that the trip is
   * exactly on time.
   * <p>
   * Example 2:
   * For the same trip instance, 3 StopTimeUpdates are provided:
   * - delay of 5 min for stop_sequence 3
   * - delay of 1 min for stop_sequence 8
   * - delay of unspecified duration for stop_sequence 10
   * This will be interpreted as:
   * - stop_sequences 3,4,5,6,7 have delay of 5 min.
   * - stop_sequences 8,9 have delay of 1 min.
   * - stop_sequences 10,... have unknown delay.
   */
  @JsonProperty("stop_time_update")
  public StopTimeUpdate[] stopTimeUpdate;
  /**
   * Additional information on the vehicle that is serving this trip.
   */
  @JsonProperty("vehicle")
  public GtfsVehicleDescriptor vehicle;
  /**
   * Moment at which the vehicle's real-time progress was measured. In POSIX
   * time (i.e., the number of seconds since January 1st 1970 00:00:00 UTC).
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
    GtfsTripUpdate that = ((GtfsTripUpdate) o);
    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(this.trip, that.trip)) {
      return false;
    }
    if (!Objects.equals(this.delay, that.delay)) {
      return false;
    }
    if (!Arrays.equals(this.stopTimeUpdate, that.stopTimeUpdate)) {
      return false;
    }
    if (!Objects.equals(this.vehicle, that.vehicle)) {
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
    result = ((result * 31) + (this.trip != null ? this.trip.hashCode() : 0));
    result = ((result * 31) + (this.delay != null ? this.delay.hashCode() : 0));
    result = ((result * 31) + Arrays.hashCode(this.stopTimeUpdate));
    result = ((result * 31) + (this.vehicle != null ? this.vehicle.hashCode() : 0));
    result = ((result * 31) + (this.timestamp != null ? this.timestamp.hashCode() : 0));
    return result;
  }
  
  
  /**
   * Timing information for a single predicted event (either arrival or
   * departure).
   * Timing consists of delay and/or estimated time, and uncertainty.
   * - delay should be used when the prediction is given relative to some
   * existing schedule in GTFS.
   * - time should be given whether there is a predicted schedule or not. If
   * both time and delay are specified, time will take precedence
   * (although normally, time, if given for a scheduled trip, should be
   * equal to scheduled time in GTFS + delay).
   * <p>
   * Uncertainty applies equally to both time and delay.
   * The uncertainty roughly specifies the expected error in true delay (but
   * note, we don't yet define its precise statistical meaning). It's possible
   * for the uncertainty to be 0, for example for trains that are driven under
   * computer timing control.
   */
  @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
  public static class StopTimeEvent
    extends GtfsObject {
    
    /**
     * Delay (in seconds) can be positive (meaning that the vehicle is late) or
     * negative (meaning that the vehicle is ahead of schedule). Delay of 0
     * means that the vehicle is exactly on time.
     */
    @JsonProperty("delay")
    public Integer delay;
    /**
     * Event as absolute time.
     * In Unix time (i.e., number of seconds since January 1st 1970 00:00:00
     * UTC).
     */
    @JsonProperty("time")
    public Long time;
    /**
     * If uncertainty is omitted, it is interpreted as unknown.
     * If the prediction is unknown or too uncertain, the delay (or time) field
     * should be empty. In such case, the uncertainty field is ignored.
     * To specify a completely certain prediction, set its uncertainty to 0.
     */
    @JsonProperty("uncertainty")
    public Integer uncertainty;
    
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      GtfsTripUpdate.StopTimeEvent that = ((GtfsTripUpdate.StopTimeEvent) o);
      if (!super.equals(o)) {
        return false;
      }
      if (!Objects.equals(this.delay, that.delay)) {
        return false;
      }
      if (!Objects.equals(this.time, that.time)) {
        return false;
      }
      if (!Objects.equals(this.uncertainty, that.uncertainty)) {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode() {
      int result = (super.hashCode());
      result = ((result * 31) + (this.delay != null ? this.delay.hashCode() : 0));
      result = ((result * 31) + (this.time != null ? this.time.hashCode() : 0));
      result = ((result * 31) + (this.uncertainty != null ? this.uncertainty.hashCode() : 0));
      return result;
    }
    
  }
  
  
  /**
   * Realtime update for arrival and/or departure events for a given stop on a
   * trip. Updates can be supplied for both past and future events.
   * The producer is allowed, although not required, to drop past events.
   */
  @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
  public static class StopTimeUpdate
    extends GtfsObject {
    
    /**
     * The update is linked to a specific stop either through stop_sequence or
     * stop_id, so one of the fields below must necessarily be set.
     * See the documentation in TripDescriptor for more information.
     * Must be the same as in stop_times.txt in the corresponding GTFS feed.
     */
    @JsonProperty("stop_sequence")
    public Integer stopSequence;
    @JsonProperty("arrival")
    public StopTimeEvent arrival;
    /**
     * Must be the same as in stops.txt in the corresponding GTFS feed.
     */
    @JsonProperty("stop_id")
    public String stopId;
    @JsonProperty("departure")
    public StopTimeEvent departure;
    @JsonProperty("schedule_relationship")
    public ScheduleRelationship scheduleRelationship;
    
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      GtfsTripUpdate.StopTimeUpdate that = ((GtfsTripUpdate.StopTimeUpdate) o);
      if (!super.equals(o)) {
        return false;
      }
      if (!Objects.equals(this.stopSequence, that.stopSequence)) {
        return false;
      }
      if (!Objects.equals(this.arrival, that.arrival)) {
        return false;
      }
      if (!Objects.equals(this.stopId, that.stopId)) {
        return false;
      }
      if (!Objects.equals(this.departure, that.departure)) {
        return false;
      }
      if (!Objects.equals(this.scheduleRelationship, that.scheduleRelationship)) {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode() {
      int result = (super.hashCode());
      result = ((result * 31) + (this.stopSequence != null ? this.stopSequence.hashCode() : 0));
      result = ((result * 31) + (this.arrival != null ? this.arrival.hashCode() : 0));
      result = ((result * 31) + (this.stopId != null ? this.stopId.hashCode() : 0));
      result = ((result * 31) + (this.departure != null ? this.departure.hashCode() : 0));
      result = ((result * 31) + (this.scheduleRelationship != null ? this.scheduleRelationship.hashCode() : 0));
      return result;
    }
    
    
    /**
     * The relation between this StopTime and the static schedule.
     */
    public enum ScheduleRelationship {
      
      SCHEDULED(0),
      SKIPPED(1),
      NO_DATA(2);
      @JsonValue
      public int value;
      
      ScheduleRelationship(int value) {
        this.value = value;
      }
      
      @JsonCreator
      public static GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship fromInt(int value) {
        switch (value) {
          case 0:
            return GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship.SCHEDULED;
          case 1:
            return GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship.SKIPPED;
          case 2:
            return GtfsTripUpdate.StopTimeUpdate.ScheduleRelationship.NO_DATA;
          default:
            return null;
        }
      }
      
    }
    
  }
  
}
