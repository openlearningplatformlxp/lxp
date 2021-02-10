package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Content source type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContentSourceType {
    /**
     * Lms content source type.
     */
    LMS,
    /**
     * Kaltura content source type.
     */
    KALTURA,
    /**
     * Allego content source type.
     */
    ALLEGO,
    /**
     * Lynda content source type.
     */
    LYNDA;

    /**
     * Names from list.
     *
     * @param contentSourceType the content source type
     * @return the list
     */
    public static List<String> namesFrom(List<ContentSourceType> contentSourceType) {
        Validate.notNull(contentSourceType);
        List<String> names = new ArrayList<>();
        for (ContentSourceType t : contentSourceType) {
            names.add(t.getName());
        }
        return names;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static ContentSourceType forValue(String name) {
        return ContentSourceType.valueOf(StringUtils.upperCase(name));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
