package com.redhat.uxl.webapp.web.filter.gzip;

import javax.servlet.ServletException;

/**
 * The type Gzip response headers not modifiable exception.
 */
public class GzipResponseHeadersNotModifiableException extends ServletException {

    /**
     * Instantiates a new Gzip response headers not modifiable exception.
     *
     * @param message the message
     */
    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
