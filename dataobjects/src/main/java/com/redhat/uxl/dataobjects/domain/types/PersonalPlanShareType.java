package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Personal plan share type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PersonalPlanShareType {
    /**
     * Manager personal plan share type.
     */
    MANAGER,
    /**
     * Direct reports personal plan share type.
     */
    DIRECT_REPORTS;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name();
    }

}
