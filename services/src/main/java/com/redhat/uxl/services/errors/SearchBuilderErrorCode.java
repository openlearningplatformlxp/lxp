package com.redhat.uxl.services.errors;

import com.redhat.uxl.commonjava.errors.ErrorType;
import com.redhat.uxl.commonjava.errors.code.ErrorCode;

/**
 * The enum Search builder error code.
 */
public enum SearchBuilderErrorCode implements ErrorCode {
    /**
     * The Invalid specification joiner.
     */
    INVALID_SPECIFICATION_JOINER(ErrorType.ET_BAD_REQUEST, "Search Builder Specification Joiner is invalid."),
    /**
     * The Invalid specification criterion operation.
     */
    INVALID_SPECIFICATION_CRITERION_OPERATION(ErrorType.ET_BAD_REQUEST,
            "Search Builder Specification Criterion Operation is invalid.");

    private static final String GROUP_NAME = "SearchBuilder";

    private String message;
    private ErrorType errorType;

    SearchBuilderErrorCode(ErrorType errorType, String message) {
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
