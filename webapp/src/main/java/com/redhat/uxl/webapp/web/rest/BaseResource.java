package com.redhat.uxl.webapp.web.rest;

import com.redhat.uxl.commonjava.errors.code.ErrorCode;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.BaseException;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.webapp.security.SecurityUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Base resource.
 */
@Slf4j
public abstract class BaseResource {
    /**
     * Handle uncaught exception object.
     *
     * @param ex       the ex
     * @param response the response
     * @return the object
     * @throws IOException the io exception
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public Object handleUncaughtException(BaseException ex, HttpServletResponse response) throws IOException {

        return handleException(ex, response);
    }

    /**
     * Handle uncaught foreign exception object.
     *
     * @param ex       the ex
     * @param response the response
     * @return the object
     * @throws IOException the io exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleUncaughtForeignException(Exception ex, HttpServletResponse response) throws IOException {
        log.error("Unhandled Foreign Exception", ex);
        GeneralException baseEx = new GeneralException(getForeignExceptionErrorCode(ex), ex.getMessage(), ex);

        return handleException(baseEx, response);
    }

    /**
     * Convert error code to http status http status.
     *
     * @param errorCode the error code
     * @return the http status
     */
    protected HttpStatus convertErrorCodeToHttpStatus(ErrorCode errorCode) {
        if (errorCode == null || errorCode.getErrorType() == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        switch (errorCode.getErrorType()) {
        case ET_BAD_REQUEST:
            return HttpStatus.BAD_REQUEST;
        case ET_FORBIDDEN:
            return HttpStatus.FORBIDDEN;
        case ET_NOT_FOUND:
            return HttpStatus.NOT_FOUND;
        case ET_UNAUTHORIZED:
            return HttpStatus.UNAUTHORIZED;
        default:
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Convert error code to http status num int.
     *
     * @param errorCode the error code
     * @return the int
     */
    protected int convertErrorCodeToHttpStatusNum(ErrorCode errorCode) {
        return convertErrorCodeToHttpStatus(errorCode).value();
    }

    // Private helper methods

    private ErrorCodeGeneral getForeignExceptionErrorCode(Exception exception) {
        if (exception instanceof AccessDeniedException) {
            if (SecurityUtils.isAuthenticated()) {
                return ErrorCodeGeneral.FORBIDDEN;
            } else {
                return ErrorCodeGeneral.UNAUTHORIZED;
            }
        }

        return ErrorCodeGeneral.FOREIGN_EXCEPTION;
    }

    private Object handleException(BaseException ex, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(convertErrorCodeToHttpStatusNum(ex.getErrorCode()));

        return ex.getExceptionDTO();
    }
}
