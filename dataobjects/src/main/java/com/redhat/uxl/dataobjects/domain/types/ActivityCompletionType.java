package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Activity completion type.
 */
public enum ActivityCompletionType {

    /**
     * Completion tracking manual activity completion type.
     */
    COMPLETION_TRACKING_MANUAL(1),
    /**
     * Completion tracking automatic activity completion type.
     */
    COMPLETION_TRACKING_AUTOMATIC(2);

    private final int value;

    ActivityCompletionType(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
