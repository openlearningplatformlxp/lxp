package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Activity display package type.
 */
public enum ActivityDisplayPackageType {

    /**
     * Display package current window activity display package type.
     */
    DISPLAY_PACKAGE_CURRENT_WINDOW(0),
    /**
     * Display new window activity display package type.
     */
    DISPLAY_NEW_WINDOW(1),
    /**
     * Display new window simple activity display package type.
     */
    DISPLAY_NEW_WINDOW_SIMPLE(2);

  private final int value;

  ActivityDisplayPackageType(int value) {
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
