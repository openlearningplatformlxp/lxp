package com.redhat.uxl.services.type;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Learning locker sort type.
 */
public enum LearningLockerSortType {
    /**
     * Asc learning locker sort type.
     */
    ASC(1),
    /**
     * Desc learning locker sort type.
     */
    DESC(-1);

    private int order;

    LearningLockerSortType(int order) {
        this.order = order;
    }

    /**
     * To value integer.
     *
     * @return the integer
     */
    @JsonValue
    public Integer toValue() {
        return this.order;
    }
}
