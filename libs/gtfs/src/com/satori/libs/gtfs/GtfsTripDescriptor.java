
package com.satori.libs.gtfs;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 *  A descriptor that identifies an instance of a GTFS trip, or all instances of
 *  a trip along a route.
 *  - To specify a single trip instance, the trip_id (and if necessary,
 *    start_time) is set. If route_id is also set, then it should be same as one
 *    that the given trip corresponds to.
 *  - To specify all the trips along a given route, only the route_id should be
 *    set. Note that if the trip_id is not known, then stop sequence ids in
 *    TripUpdate are not sufficient, and stop_ids must be provided as well. In
 *    addition, absolute arrival/departure times must be provided.
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class GtfsTripDescriptor
    extends GtfsObject
{

    /**
     *  The initially scheduled start time of this trip instance.
     *  When the trip_id corresponds to a non-frequency-based trip, this field
     *  should either be omitted or be equal to the value in the GTFS feed. When
     *  the trip_id correponds to a frequency-based trip, the start_time must be
     *  specified for trip updates and vehicle positions. If the trip corresponds
     *  to exact_times=1 GTFS record, then start_time must be some multiple
     *  (including zero) of headway_secs later than frequencies.txt start_time for
     *  the corresponding time period. If the trip corresponds to exact_times=0,
     *  then its start_time may be arbitrary, and is initially expected to be the
     *  first departure of the trip. Once established, the start_time of this
     *  frequency-based trip should be considered immutable, even if the first
     *  departure time changes -- that time change may instead be reflected in a
     *  StopTimeUpdate.
     *  Format and semantics of the field is same as that of
     *  GTFS/frequencies.txt/start_time, e.g., 11:15:35 or 25:15:35.
     * 
     * 
     */
    @JsonProperty("start_time")
    public String startTime;
    /**
     *  The trip_id from the GTFS feed that this selector refers to.
     *  For non frequency-based trips, this field is enough to uniquely identify
     *  the trip. For frequency-based trip, start_time and start_date might also be
     *  necessary.
     * 
     * 
     */
    @JsonProperty("trip_id")
    public String tripId;
    /**
     *  The direction_id from the GTFS feed trips.txt file, indicating the
     *  direction of travel for trips this selector refers to. This field is
     *  still experimental, and subject to change. It may be formally adopted in
     *  the future.
     * 
     * 
     */
    @JsonProperty("direction_id")
    public Integer directionId;
    /**
     *  The route_id from the GTFS that this selector refers to.
     * 
     * 
     */
    @JsonProperty("route_id")
    public String routeId;
    @JsonProperty("schedule_relationship")
    public ScheduleRelationship scheduleRelationship;
    /**
     *  The scheduled start date of this trip instance.
     *  Must be provided to disambiguate trips that are so late as to collide with
     *  a scheduled trip on a next day. For example, for a train that departs 8:00
     *  and 20:00 every day, and is 12 hours late, there would be two distinct
     *  trips on the same time.
     *  This field can be provided but is not mandatory for schedules in which such
     *  collisions are impossible - for example, a service running on hourly
     *  schedule where a vehicle that is one hour late is not considered to be
     *  related to schedule anymore.
     *  In YYYYMMDD format.
     * 
     * 
     */
    @JsonProperty("start_date")
    public String startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsTripDescriptor that = ((GtfsTripDescriptor) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Objects.equals(this.startTime, that.startTime)) {
            return false;
        }
        if (!Objects.equals(this.tripId, that.tripId)) {
            return false;
        }
        if (!Objects.equals(this.directionId, that.directionId)) {
            return false;
        }
        if (!Objects.equals(this.routeId, that.routeId)) {
            return false;
        }
        if (!Objects.equals(this.scheduleRelationship, that.scheduleRelationship)) {
            return false;
        }
        if (!Objects.equals(this.startDate, that.startDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.hashCode());
        result = ((result* 31)+(this.startTime != null ? this.startTime.hashCode() : 0));
        result = ((result* 31)+(this.tripId != null ? this.tripId.hashCode() : 0));
        result = ((result* 31)+(this.directionId != null ? this.directionId.hashCode() : 0));
        result = ((result* 31)+(this.routeId != null ? this.routeId.hashCode() : 0));
        result = ((result* 31)+(this.scheduleRelationship != null ? this.scheduleRelationship.hashCode() : 0));
        result = ((result* 31)+(this.startDate != null ? this.startDate.hashCode() : 0));
        return result;
    }


    /**
     *  The relation between this trip and the static schedule. If a trip is done
     *  in accordance with temporary schedule, not reflected in GTFS, then it
     *  shouldn't be marked as SCHEDULED, but likely as ADDED.
     * 
     * 
     */
    public enum ScheduleRelationship {

        SCHEDULED(0),
        ADDED(1),
        UNSCHEDULED(2),
        CANCELED(3);
        @JsonValue
        public int value;

        ScheduleRelationship(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsTripDescriptor.ScheduleRelationship fromInt(int value) {
            switch (value) {
                case  0 :
                    return GtfsTripDescriptor.ScheduleRelationship.SCHEDULED;
                case  1 :
                    return GtfsTripDescriptor.ScheduleRelationship.ADDED;
                case  2 :
                    return GtfsTripDescriptor.ScheduleRelationship.UNSCHEDULED;
                case  3 :
                    return GtfsTripDescriptor.ScheduleRelationship.CANCELED;
                default:
                    return null;
            }
        }

    }

}
