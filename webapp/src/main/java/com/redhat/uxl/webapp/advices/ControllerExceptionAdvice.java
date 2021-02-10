package com.redhat.uxl.webapp.advices;

import com.redhat.uxl.commonjava.errors.exception.BaseException;
import com.redhat.uxl.webapp.advices.dto.ResponseErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Controller exception advice.
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {
    /**
     * Handle api exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({ BaseException.class })
    protected ResponseEntity<ResponseErrorDTO> handleApiException(BaseException ex, WebRequest request) {
        log.debug("Api error response " + ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode().getErrorType().getCode())
                .body(new ResponseErrorDTO(ex.getErrorCode().getErrorType().getCode(), ex.getMessage()));
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("Jackson error response " + ex.getMessage(), ex);
        String[] phrases = StringUtils.splitByWholeSeparator(ex.getRootCause().getMessage(), " String ");
        String error = "Cannot deserialize value " + StringUtils.splitByWholeSeparator(phrases[1], "\n")[0];
        return ResponseEntity.status(400).body(new ResponseErrorDTO(400, error));
    }
}
