
package com.satori.libs.gtfs;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 
 *  Low level data structures used above.
 * 
 *  A time interval. The interval is considered active at time 't' if 't' is
 *  greater than or equal to the start time and less than the end time.
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class GtfsTimeRange
    extends GtfsObject
{

    /**
     *  Start time, in POSIX time (i.e., number of seconds since January 1st 1970
     *  00:00:00 UTC).
     *  If missing, the interval starts at minus infinity.
     * 
     * 
     */
    @JsonProperty("start")
    public Long start;
    /**
     *  End time, in POSIX time (i.e., number of seconds since January 1st 1970
     *  00:00:00 UTC).
     *  If missing, the interval ends at plus infinity.
     * 
     * 
     */
    @JsonProperty("end")
    public Long end;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsTimeRange that = ((GtfsTimeRange) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Objects.equals(this.start, that.start)) {
            return false;
        }
        if (!Objects.equals(this.end, that.end)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.hashCode());
        result = ((result* 31)+(this.start != null ? this.start.hashCode() : 0));
        result = ((result* 31)+(this.end != null ? this.end.hashCode() : 0));
        return result;
    }

}
