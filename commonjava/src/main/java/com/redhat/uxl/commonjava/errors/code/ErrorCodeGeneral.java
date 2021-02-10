package com.redhat.uxl.commonjava.errors.code;

import com.redhat.uxl.commonjava.errors.ErrorType;

/**
 * The enum Error code general.
 */
public enum ErrorCodeGeneral implements ErrorCode {
    /**
     * The Bad request.
     */
    BAD_REQUEST(ErrorType.ET_BAD_REQUEST, "Bad Request"),
    /**
     * Forbidden error code general.
     */
    FORBIDDEN(ErrorType.ET_FORBIDDEN, "Forbidden"),
    /**
     * The Foreign exception.
     */
    FOREIGN_EXCEPTION(ErrorType.ET_INTERNAL_SERVER_ERROR, "Internal Server Error"),
    /**
     * The Internal server error.
     */
    INTERNAL_SERVER_ERROR(ErrorType.ET_INTERNAL_SERVER_ERROR, "Internal Server Error"),
    /**
     * The Not found.
     */
    NOT_FOUND(ErrorType.ET_NOT_FOUND, "Not Found"),
    /**
     * The Not implemented.
     */
    NOT_IMPLEMENTED(ErrorType.ET_NOT_IMPLEMENTED, "Not Implemented"),
    /**
     * Unauthorized error code general.
     */
    UNAUTHORIZED(ErrorType.ET_UNAUTHORIZED, "Unauthorized");

    private static final String GROUP_NAME = "General";

    private String message;
    private ErrorType errorType;

    private ErrorCodeGeneral(ErrorType errorType, String message) {
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
