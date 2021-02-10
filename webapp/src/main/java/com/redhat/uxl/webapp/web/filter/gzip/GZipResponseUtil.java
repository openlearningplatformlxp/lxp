package com.redhat.uxl.webapp.web.filter.gzip;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type G zip response util.
 */
@Slf4j
public final class GZipResponseUtil {

    private static final int EMPTY_GZIPPED_CONTENT_SIZE = 20;

    private GZipResponseUtil() {
    }

    /**
     * Should gzipped body be zero boolean.
     *
     * @param compressedBytes the compressed bytes
     * @param request         the request
     * @return the boolean
     */
    public static boolean shouldGzippedBodyBeZero(byte[] compressedBytes, HttpServletRequest request) {

        // Check for 0 length body
        if (compressedBytes.length == EMPTY_GZIPPED_CONTENT_SIZE) {
            if (log.isTraceEnabled()) {
                log.trace("{} resulted in an empty response.", request.getRequestURL());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Should body be zero boolean.
     *
     * @param request        the request
     * @param responseStatus the response status
     * @return the boolean
     */
    public static boolean shouldBodyBeZero(HttpServletRequest request, int responseStatus) {

        // Check for NO_CONTENT
        if (responseStatus == HttpServletResponse.SC_NO_CONTENT) {
            if (log.isDebugEnabled()) {
                log.debug("{} resulted in a {} response. Removing message body in accordance with RFC2616.",
                        request.getRequestURL(), HttpServletResponse.SC_NO_CONTENT);
            }
            return true;
        }

        // Check for NOT_MODIFIED
        if (responseStatus == HttpServletResponse.SC_NOT_MODIFIED) {
            if (log.isDebugEnabled()) {
                log.debug("{} resulted in a {} response. Removing message body in accordance with RFC2616.",
                        request.getRequestURL(), HttpServletResponse.SC_NOT_MODIFIED);
            }
            return true;
        }
        return false;
    }

    /**
     * Add gzip header.
     *
     * @param response the response
     * @throws GzipResponseHeadersNotModifiableException the gzip response headers not modifiable exception
     */
    public static void addGzipHeader(HttpServletResponse response) throws GzipResponseHeadersNotModifiableException {
        response.setHeader("Content-Encoding", "gzip");
        boolean containsEncoding = response.containsHeader("Content-Encoding");
        if (!containsEncoding) {
            throw new GzipResponseHeadersNotModifiableException(
                    "Failure when attempting to set " + "Content-Encoding: gzip");
        }
    }
}
