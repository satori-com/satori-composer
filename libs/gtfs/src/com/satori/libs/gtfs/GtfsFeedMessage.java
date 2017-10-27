
package com.satori.libs.gtfs;

import java.util.Arrays;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *  The contents of a feed message.
 *  A feed is a continuous stream of feed messages. Each message in the stream is
 *  obtained as a response to an appropriate HTTP GET request.
 *  A realtime feed is always defined with relation to an existing GTFS feed.
 *  All the entity ids are resolved with respect to the GTFS feed.
 * 
 *  A feed depends on some external configuration:
 *  - The corresponding GTFS feed.
 *  - Feed application (updates, positions or alerts). A feed should contain only
 *    items of one specified application; all the other entities will be ignored.
 *  - Polling frequency
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class GtfsFeedMessage
    extends GtfsObject
{

    /**
     *  Metadata about this feed and feed message.
     * 
     * 
     */
    @JsonProperty("header")
    public GtfsFeedHeader header;
    /**
     *  Contents of the feed.
     * 
     * 
     */
    @JsonProperty("entity")
    public GtfsFeedEntity[] entity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsFeedMessage that = ((GtfsFeedMessage) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Objects.equals(this.header, that.header)) {
            return false;
        }
        if (!Arrays.equals(this.entity, that.entity)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.hashCode());
        result = ((result* 31)+(this.header != null ? this.header.hashCode() : 0));
        result = ((result* 31)+ Arrays.hashCode(this.entity));
        return result;
    }

}
