package com.redhat.uxl.commonjava.errors.exception;

import com.redhat.uxl.commonjava.errors.code.ErrorCode;

/**
 * The type General exception.
 */
public class GeneralException extends BaseException {

    /**
     * Instantiates a new General exception.
     *
     * @param errorCode the error code
     */
    public GeneralException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Instantiates a new General exception.
     *
     * @param errorCode the error code
     * @param cause     the cause
     */
    public GeneralException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Instantiates a new General exception.
     *
     * @param errorCode the error code
     * @param message   the message
     */
    public GeneralException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Instantiates a new General exception.
     *
     * @param errorCode the error code
     * @param message   the message
     * @param cause     the cause
     */
    public GeneralException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
