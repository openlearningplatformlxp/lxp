package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Course document visibility type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CourseDocumentVisibilityType {
    /**
     * Public course document visibility type.
     */
    PUBLIC,
    /**
     * Restricted audience course document visibility type.
     */
    RESTRICTED_AUDIENCE;

    /**
     * Names from list.
     *
     * @param types the types
     * @return the list
     */
    public static List<String> namesFrom(List<CourseDocumentVisibilityType> types) {
        Validate.notNull(types);
        List<String> names = new ArrayList<>();
        for (CourseDocumentVisibilityType t : types) {
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
}
