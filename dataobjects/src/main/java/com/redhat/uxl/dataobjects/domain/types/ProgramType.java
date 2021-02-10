package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Program type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProgramType {
    /**
     * Learning path program type.
     */
    LEARNING_PATH,
    /**
     * Classroom program type.
     */
    CLASSROOM,
    /**
     * Course program type.
     */
    COURSE,
    /**
     * Evidence program type.
     */
    EVIDENCE,
    /**
     * Wikipage program type.
     */
    WIKIPAGE;

    /**
     * Names from list.
     *
     * @param types the types
     * @return the list
     */
    public static List<String> namesFrom(List<ProgramType> types) {
        Validate.notNull(types);
        List<String> names = new ArrayList<>();
        for (ProgramType t : types) {
            names.add(t.getName());
        }
        return names;
    }

    /**
     * Value of program type.
     *
     * @param type the type
     * @return the program type
     */
    public static ProgramType valueOf(ProgramCourseType type) {
        if (type != null) {
            switch (type) {
            case COURSE:
            case WEB_TASK:
            case TEXT_ENTRY:
            case SINGLE_ACTIVITY_COURSE:
            case OFFLINE_TASK:
            case WIKIPAGE:
                return COURSE;
            case PROGRAM_LINK:
                return LEARNING_PATH;
            }
        }
        return null;
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
    public static ProgramType forValue(String name) {
        return ProgramType.valueOf(StringUtils.upperCase(name));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
