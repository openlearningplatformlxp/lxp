package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Totara course set next set operator type.
 */
public enum TotaraCourseSetNextSetOperatorType {

    /**
     * Nextsetoperator then totara course set next set operator type.
     */
    NEXTSETOPERATOR_THEN(1),
    /**
     * Nextsetoperator or totara course set next set operator type.
     */
    NEXTSETOPERATOR_OR(2),
    /**
     * Nextsetoperator and totara course set next set operator type.
     */
    NEXTSETOPERATOR_AND(3);

    private Integer value;

    TotaraCourseSetNextSetOperatorType(Integer value) {
        this.value = value;
    }

    /**
     * From integer totara course set next set operator type.
     *
     * @param intValue the int value
     * @return the totara course set next set operator type
     */
    public static TotaraCourseSetNextSetOperatorType fromInteger(Integer intValue) {
        for (TotaraCourseSetNextSetOperatorType type : TotaraCourseSetNextSetOperatorType.values()) {
            if (intValue.equals(type.value)) {
                return type;
            }
        }
        return null;
    }
}
