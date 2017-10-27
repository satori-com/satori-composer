
package com.satori.libs.gtfs;

import java.util.Arrays;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 *  An alert, indicating some sort of incident in the public transit network.
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class GtfsAlert
    extends GtfsObject
{

    /**
     *  Time when the alert should be shown to the user. If missing, the
     *  alert will be shown as long as it appears in the feed.
     *  If multiple ranges are given, the alert will be shown during all of them.
     * 
     * 
     */
    @JsonProperty("active_period")
    public GtfsTimeRange[] activePeriod;
    @JsonProperty("effect")
    public Effect effect;
    @JsonProperty("cause")
    public Cause cause;
    /**
     *  Full description for the alert as plain-text. The information in the
     *  description should add to the information of the header.
     * 
     * 
     */
    @JsonProperty("description_text")
    public GtfsTranslatedString descriptionText;
    /**
     *  Alert header. Contains a short summary of the alert text as plain-text.
     * 
     * 
     */
    @JsonProperty("header_text")
    public GtfsTranslatedString headerText;
    /**
     *  Entities whose users we should notify of this alert.
     * 
     * 
     */
    @JsonProperty("informed_entity")
    public GtfsEntitySelector[] informedEntity;
    /**
     *  The URL which provides additional information about the alert.
     * 
     * 
     */
    @JsonProperty("url")
    public GtfsTranslatedString url;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsAlert that = ((GtfsAlert) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Arrays.equals(this.activePeriod, that.activePeriod)) {
            return false;
        }
        if (!Objects.equals(this.effect, that.effect)) {
            return false;
        }
        if (!Objects.equals(this.cause, that.cause)) {
            return false;
        }
        if (!Objects.equals(this.descriptionText, that.descriptionText)) {
            return false;
        }
        if (!Objects.equals(this.headerText, that.headerText)) {
            return false;
        }
        if (!Arrays.equals(this.informedEntity, that.informedEntity)) {
            return false;
        }
        if (!Objects.equals(this.url, that.url)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.hashCode());
        result = ((result* 31)+ Arrays.hashCode(this.activePeriod));
        result = ((result* 31)+(this.effect != null ? this.effect.hashCode() : 0));
        result = ((result* 31)+(this.cause != null ? this.cause.hashCode() : 0));
        result = ((result* 31)+(this.descriptionText != null ? this.descriptionText.hashCode() : 0));
        result = ((result* 31)+(this.headerText != null ? this.headerText.hashCode() : 0));
        result = ((result* 31)+ Arrays.hashCode(this.informedEntity));
        result = ((result* 31)+(this.url != null ? this.url.hashCode() : 0));
        return result;
    }


    /**
     *  Cause of this alert.
     * 
     * 
     */
    public enum Cause {

        UNKNOWN_CAUSE(1),
        OTHER_CAUSE(2),
        TECHNICAL_PROBLEM(3),
        STRIKE(4),
        DEMONSTRATION(5),
        ACCIDENT(6),
        HOLIDAY(7),
        WEATHER(8),
        MAINTENANCE(9),
        CONSTRUCTION(10),
        POLICE_ACTIVITY(11),
        MEDICAL_EMERGENCY(12);
        @JsonValue
        public int value;

        Cause(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsAlert.Cause fromInt(int value) {
            switch (value) {
                case  1 :
                    return GtfsAlert.Cause.UNKNOWN_CAUSE;
                case  2 :
                    return GtfsAlert.Cause.OTHER_CAUSE;
                case  3 :
                    return GtfsAlert.Cause.TECHNICAL_PROBLEM;
                case  4 :
                    return GtfsAlert.Cause.STRIKE;
                case  5 :
                    return GtfsAlert.Cause.DEMONSTRATION;
                case  6 :
                    return GtfsAlert.Cause.ACCIDENT;
                case  7 :
                    return GtfsAlert.Cause.HOLIDAY;
                case  8 :
                    return GtfsAlert.Cause.WEATHER;
                case  9 :
                    return GtfsAlert.Cause.MAINTENANCE;
                case  10 :
                    return GtfsAlert.Cause.CONSTRUCTION;
                case  11 :
                    return GtfsAlert.Cause.POLICE_ACTIVITY;
                case  12 :
                    return GtfsAlert.Cause.MEDICAL_EMERGENCY;
                default:
                    return null;
            }
        }

    }


    /**
     *  What is the effect of this problem on the affected entity.
     * 
     * 
     */
    public enum Effect {

        NO_SERVICE(1),
        REDUCED_SERVICE(2),
        SIGNIFICANT_DELAYS(3),
        DETOUR(4),
        ADDITIONAL_SERVICE(5),
        MODIFIED_SERVICE(6),
        OTHER_EFFECT(7),
        UNKNOWN_EFFECT(8),
        STOP_MOVED(9);
        @JsonValue
        public int value;

        Effect(int value) {
            this.value = value;
        }

        @JsonCreator
        public static GtfsAlert.Effect fromInt(int value) {
            switch (value) {
                case  1 :
                    return GtfsAlert.Effect.NO_SERVICE;
                case  2 :
                    return GtfsAlert.Effect.REDUCED_SERVICE;
                case  3 :
                    return GtfsAlert.Effect.SIGNIFICANT_DELAYS;
                case  4 :
                    return GtfsAlert.Effect.DETOUR;
                case  5 :
                    return GtfsAlert.Effect.ADDITIONAL_SERVICE;
                case  6 :
                    return GtfsAlert.Effect.MODIFIED_SERVICE;
                case  7 :
                    return GtfsAlert.Effect.OTHER_EFFECT;
                case  8 :
                    return GtfsAlert.Effect.UNKNOWN_EFFECT;
                case  9 :
                    return GtfsAlert.Effect.STOP_MOVED;
                default:
                    return null;
            }
        }

    }

}
