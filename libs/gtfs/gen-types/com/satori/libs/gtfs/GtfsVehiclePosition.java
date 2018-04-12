
package com.satori.libs.gtfs;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 *  Realtime positioning information for a given vehicle.
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class GtfsVehiclePosition
    extends GtfsObject
{

    /**
     *  The Trip that this vehicle is serving.
     *  Can be empty or partial if the vehicle can not be identified with a given
     *  trip instance.
     * 
     * 
     */
    @JsonProperty("trip")
    public GtfsTripDescriptor trip;
    /**
     *  The stop sequence index of the current stop. The meaning of
     *  current_stop_sequence (i.e., the stop that it refers to) is determined by
     *  current_status.
     *  If current_status is missing IN_TRANSIT_TO is assumed.
     * 
     * 
     */
    @JsonProperty("current_stop_sequence")
    public Integer currentStopSequence;
    @JsonProperty("congestion_level")
    public CongestionLevel congestionLevel;
    @JsonProperty("occupancy_status")
    public OccupancyStatus occupancyStatus;
    /**
     *  Identifies the current stop. The value must be the same as in stops.txt in
     *  the corresponding GTFS feed.
     * 
     * 
     */
    @JsonProperty("stop_id")
    public String stopId;
    /**
     *  The exact status of the vehicle with respect to the current stop.
     *  Ignored if current_stop_sequence is missing.
     * 
     * 
     */
    @JsonProperty("current_status")
    public VehicleStopStatus currentStatus;
    /**
     *  Current position of this vehicle.
     * 
     * 
     */
    @JsonProperty("position")
    public GtfsPosition position;
    /**
     *  Additional information on the vehicle that is serving this trip.
     * 
     * 
     */
    @JsonProperty("vehicle")
    public GtfsVehicleDescriptor vehicle;
    /**
     *  Moment at which the vehicle's position was measured. In POSIX time
     *  (i.e., number of seconds since January 1st 1970 00:00:00 UTC).
     * 
     * 
     */
    @JsonProperty("timestamp")
    public Long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsVehiclePosition that = ((GtfsVehiclePosition) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Objects.equals(this.trip, that.trip)) {
            return false;
        }
        if (!Objects.equals(this.currentStopSequence, that.currentStopSequence)) {
            return false;
        }
        if (!Objects.equals(this.congestionLevel, that.congestionLevel)) {
            return false;
        }
        if (!Objects.equals(this.occupancyStatus, that.occupancyStatus)) {
            return false;
        }
        if (!Objects.equals(this.stopId, that.stopId)) {
            return false;
        }
        if (!Objects.equals(this.currentStatus, that.currentStatus)) {
            return false;
        }
        if (!Objects.equals(this.position, that.position)) {
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
        result = ((result* 31)+(this.trip != null ? this.trip.hashCode() : 0));
        result = ((result* 31)+(this.currentStopSequence != null ? this.currentStopSequence.hashCode() : 0));
        result = ((result* 31)+(this.congestionLevel != null ? this.congestionLevel.hashCode() : 0));
        result = ((result* 31)+(this.occupancyStatus != null ? this.occupancyStatus.hashCode() : 0));
        result = ((result* 31)+(this.stopId != null ? this.stopId.hashCode() : 0));
        result = ((result* 31)+(this.currentStatus != null ? this.currentStatus.hashCode() : 0));
        result = ((result* 31)+(this.position != null ? this.position.hashCode() : 0));
        result = ((result* 31)+(this.vehicle != null ? this.vehicle.hashCode() : 0));
        result = ((result* 31)+(this.timestamp != null ? this.timestamp.hashCode() : 0));
        return result;
    }


    /**
     *  Congestion level that is affecting this vehicle.
     * 
     * 
     */
    public enum CongestionLevel {

        UNKNOWN_CONGESTION_LEVEL(0),
        RUNNING_SMOOTHLY(1),
        STOP_AND_GO(2),
        CONGESTION(3),
        SEVERE_CONGESTION(4);
        @JsonValue
        public int value;

        CongestionLevel(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsVehiclePosition.CongestionLevel fromInt(int value) {
            switch (value) {
                case  0 :
                    return GtfsVehiclePosition.CongestionLevel.UNKNOWN_CONGESTION_LEVEL;
                case  1 :
                    return GtfsVehiclePosition.CongestionLevel.RUNNING_SMOOTHLY;
                case  2 :
                    return GtfsVehiclePosition.CongestionLevel.STOP_AND_GO;
                case  3 :
                    return GtfsVehiclePosition.CongestionLevel.CONGESTION;
                case  4 :
                    return GtfsVehiclePosition.CongestionLevel.SEVERE_CONGESTION;
                default:
                    return null;
            }
        }

    }


    /**
     *  The degree of passenger occupancy of the vehicle. This field is still
     *  experimental, and subject to change. It may be formally adopted in the
     *  future.
     * 
     * 
     */
    public enum OccupancyStatus {

        EMPTY(0),
        MANY_SEATS_AVAILABLE(1),
        FEW_SEATS_AVAILABLE(2),
        STANDING_ROOM_ONLY(3),
        CRUSHED_STANDING_ROOM_ONLY(4),
        FULL(5),
        NOT_ACCEPTING_PASSENGERS(6);
        @JsonValue
        public int value;

        OccupancyStatus(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsVehiclePosition.OccupancyStatus fromInt(int value) {
            switch (value) {
                case  0 :
                    return GtfsVehiclePosition.OccupancyStatus.EMPTY;
                case  1 :
                    return GtfsVehiclePosition.OccupancyStatus.MANY_SEATS_AVAILABLE;
                case  2 :
                    return GtfsVehiclePosition.OccupancyStatus.FEW_SEATS_AVAILABLE;
                case  3 :
                    return GtfsVehiclePosition.OccupancyStatus.STANDING_ROOM_ONLY;
                case  4 :
                    return GtfsVehiclePosition.OccupancyStatus.CRUSHED_STANDING_ROOM_ONLY;
                case  5 :
                    return GtfsVehiclePosition.OccupancyStatus.FULL;
                case  6 :
                    return GtfsVehiclePosition.OccupancyStatus.NOT_ACCEPTING_PASSENGERS;
                default:
                    return null;
            }
        }

    }

    public enum VehicleStopStatus {

        INCOMING_AT(0),
        STOPPED_AT(1),
        IN_TRANSIT_TO(2);
        @JsonValue
        public int value;

        VehicleStopStatus(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsVehiclePosition.VehicleStopStatus fromInt(int value) {
            switch (value) {
                case  0 :
                    return GtfsVehiclePosition.VehicleStopStatus.INCOMING_AT;
                case  1 :
                    return GtfsVehiclePosition.VehicleStopStatus.STOPPED_AT;
                case  2 :
                    return GtfsVehiclePosition.VehicleStopStatus.IN_TRANSIT_TO;
                default:
                    return null;
            }
        }

    }

}
