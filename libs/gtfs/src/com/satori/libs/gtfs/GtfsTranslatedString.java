
package com.satori.libs.gtfs;

import java.util.Arrays;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *  An internationalized message containing per-language versions of a snippet of
 *  text or a URL.
 *  One of the strings from a message will be picked up. The resolution proceeds
 *  as follows:
 *  1. If the UI language matches the language code of a translation,
 *     the first matching translation is picked.
 *  2. If a default UI language (e.g., English) matches the language code of a
 *     translation, the first matching translation is picked.
 *  3. If some translation has an unspecified language code, that translation is
 *     picked.
 * 
 * 
 */
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class GtfsTranslatedString
    extends GtfsObject
{

    /**
     *  At least one translation must be provided.
     * 
     * 
     */
    @JsonProperty("translation")
    public Translation[] translation;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null)||(getClass() != o.getClass())) {
            return false;
        }
        GtfsTranslatedString that = ((GtfsTranslatedString) o);
        if (!super.equals(o)) {
          return false;
        }
        if (!Arrays.equals(this.translation, that.translation)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (super.hashCode());
        result = ((result* 31)+ Arrays.hashCode(this.translation));
        return result;
    }

    @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    public static class Translation
        extends GtfsObject
    {

        /**
         *  BCP-47 language code. Can be omitted if the language is unknown or if
         *  no i18n is done at all for the feed. At most one translation is
         *  allowed to have an unspecified language tag.
         * 
         * 
         */
        @JsonProperty("language")
        public String language;
        /**
         *  A UTF-8 string containing the message.
         * 
         * 
         */
        @JsonProperty("text")
        public String text;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null)||(getClass() != o.getClass())) {
                return false;
            }
            GtfsTranslatedString.Translation that = ((GtfsTranslatedString.Translation) o);
            if (!super.equals(o)) {
              return false;
            }
            if (!Objects.equals(this.language, that.language)) {
                return false;
            }
            if (!Objects.equals(this.text, that.text)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = (super.hashCode());
            result = ((result* 31)+(this.language != null ? this.language.hashCode() : 0));
            result = ((result* 31)+(this.text != null ? this.text.hashCode() : 0));
            return result;
        }

    }

}
