package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Learning locker job execution type.
 */
public enum LearningLockerJobExecutionType {
    /**
     * Kaltura learning locker job execution type.
     */
    KALTURA(0),
    /**
     * Lynda learning locker job execution type.
     */
    LYNDA(1),
    /**
     * Allego learning locker job execution type.
     */
    ALLEGO(2);

  private final int value;

  LearningLockerJobExecutionType(int value) {
    this.value = value;
  }

}
