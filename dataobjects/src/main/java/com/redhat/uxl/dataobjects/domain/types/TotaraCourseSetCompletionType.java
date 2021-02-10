package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Totara course set completion type.
 */
public enum TotaraCourseSetCompletionType {

    /**
     * Completiontype all totara course set completion type.
     */
    COMPLETIONTYPE_ALL(1),
    /**
     * Completiontype any totara course set completion type.
     */
    COMPLETIONTYPE_ANY(2),
    /**
     * Completiontype some totara course set completion type.
     */
    COMPLETIONTYPE_SOME(3),
    /**
     * Completiontype optional totara course set completion type.
     */
    COMPLETIONTYPE_OPTIONAL(4);

    private Integer value;

    TotaraCourseSetCompletionType(Integer value) {
        this.value = value;
    }

    /**
     * From integer totara course set completion type.
     *
     * @param intValue the int value
     * @return the totara course set completion type
     */
    public static TotaraCourseSetCompletionType fromInteger(Integer intValue) {
        for (TotaraCourseSetCompletionType type : TotaraCourseSetCompletionType.values()) {
            if (intValue.equals(type.value)) {
                return type;
            }
        }
        return null;
    }

}
