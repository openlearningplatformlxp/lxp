package com.redhat.uxl.services.errors;

import com.redhat.uxl.commonjava.errors.ErrorType;
import com.redhat.uxl.commonjava.errors.code.ErrorCode;

/**
 * The enum Person service error code.
 */
public enum PersonServiceErrorCode implements ErrorCode {
    /**
     * The Bad password.
     */
    BAD_PASSWORD(ErrorType.ET_FORBIDDEN, "Password is incorrect."),
    /**
     * The Person disabled.
     */
    PERSON_DISABLED(ErrorType.ET_FORBIDDEN, "Account is disabled.");

    private static final String GROUP_NAME = "PersonService";

    private String message;
    private ErrorType errorType;

    private PersonServiceErrorCode(ErrorType errorType, String message) {
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
