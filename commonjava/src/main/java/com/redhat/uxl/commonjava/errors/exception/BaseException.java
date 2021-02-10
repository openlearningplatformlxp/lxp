package com.redhat.uxl.commonjava.errors.exception;

import com.redhat.uxl.commonjava.errors.code.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * The type Base exception.
 */
@Data
public abstract class BaseException extends RuntimeException {

    private ErrorCode errorCode;
    private Map<String, String> messageArgs = new HashMap<>();

    /**
     * Instantiates a new Base exception.
     *
     * @param errorCode the error code
     */
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());

        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param errorCode the error code
     * @param cause     the cause
     */
    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);

        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param errorCode the error code
     * @param message   the message
     */
    public BaseException(ErrorCode errorCode, String message) {
        super(message);

        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param errorCode the error code
     * @param message   the message
     * @param cause     the cause
     */
    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);

        setErrorCode(errorCode);
    }

    /**
     * Gets exception dto.
     *
     * @return the exception dto
     */
    public ExceptionDTO getExceptionDTO() {
        Throwable cause = getCause();

        if (cause == null) {
            cause = new Throwable(getMessage());

            cause.setStackTrace(this.getStackTrace());
        }

        return new ExceptionDTO(getErrorCode().getGroupName(), getErrorCode().getName(), getMessage(), getMessageArgs(),
                cause);
    }

    @Data
    private static class ExceptionDTO {
        private final boolean error = true;

        private String groupName;
        private String errorCode;
        private String message;
        private Map<String, String> messageArgs;
        private Throwable cause;

        /**
         * Instantiates a new Exception dto.
         *
         * @param groupName   the group name
         * @param errorCode   the error code
         * @param message     the message
         * @param messageArgs the message args
         * @param cause       the cause
         */
        public ExceptionDTO(String groupName, String errorCode, String message, Map<String, String> messageArgs,
                Throwable cause) {
            setGroupName(groupName.toLowerCase());
            setErrorCode(errorCode.toLowerCase());
            setMessage(message);
            setMessageArgs(messageArgs);
            setCause(cause);
        }
    }
}
