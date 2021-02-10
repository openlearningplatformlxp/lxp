package com.redhat.uxl.commonjava.errors;

/**
 * The enum Error type.
 */
public enum ErrorType {
    /**
     * Et bad request error type.
     */
    ET_BAD_REQUEST(400),
    /**
     * Et forbidden error type.
     */
    ET_FORBIDDEN(403),
    /**
     * Et internal server error error type.
     */
    ET_INTERNAL_SERVER_ERROR(500),
    /**
     * Et not found error type.
     */
    ET_NOT_FOUND(404),
    /**
     * Et not implemented error type.
     */
    ET_NOT_IMPLEMENTED(503),
    /**
     * Et unauthorized error type.
     */
    ET_UNAUTHORIZED(401);

    /**
     * The Code.
     */
    int code;

    ErrorType(int code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}
