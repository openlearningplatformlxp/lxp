package com.redhat.uxl.commonjava.errors.exception;

import com.redhat.uxl.commonjava.errors.ErrorType;
import com.redhat.uxl.commonjava.errors.code.ErrorCode;

/**
 * The enum General error code.
 */
public enum GeneralErrorCode implements ErrorCode {
    /**
     * The Entity not found.
     */
    ENTITY_NOT_FOUND(ErrorType.ET_NOT_FOUND, "Entity not found.");

  private static final String GROUP_NAME = "General";

  private String message;
  private ErrorType errorType;

  private GeneralErrorCode(ErrorType errorType, String message) {
    this.errorType = errorType;
    this.message = message;
  }

  @Override
  public ErrorType getErrorType() {
    return errorType;
  }

  @Override
  public String getGroupName() {
    return GROUP_NAME;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getName() {
    return name();
  }
}
