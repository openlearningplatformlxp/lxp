package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Activity display type.
 */
public enum ActivityDisplayType {

    /**
     * Display automatic activity display type.
     */
    DISPLAY_AUTOMATIC(0),
    /**
     * Display embed activity display type.
     */
    DISPLAY_EMBED(1),
    /**
     * Display in frame activity display type.
     */
    DISPLAY_IN_FRAME(2),
    /**
     * Display new window activity display type.
     */
    DISPLAY_NEW_WINDOW(3),
    /**
     * Display force download activity display type.
     */
    DISPLAY_FORCE_DOWNLOAD(4),
    /**
     * Display open activity display type.
     */
    DISPLAY_OPEN(5),
    /**
     * Display in popup activity display type.
     */
    DISPLAY_IN_POPUP(6);

    private final int value;

    ActivityDisplayType(int value) {
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
