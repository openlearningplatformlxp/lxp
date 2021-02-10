package com.redhat.uxl.commonjava.errors.code;

import com.redhat.uxl.commonjava.errors.ErrorType;

/**
 * The interface Error code.
 */
public interface ErrorCode {
    /**
     * Gets error type.
     *
     * @return the error type
     */
    ErrorType getErrorType();

    /**
     * Gets group name.
     *
     * @return the group name
     */
    String getGroupName();

    /**
     * Gets message.
     *
     * @return the message
     */
    String getMessage();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();
}
