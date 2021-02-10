package com.redhat.uxl.webapp.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * The type Header util.
 */
public final class HeaderUtil {
    private HeaderUtil() {
    }

    /**
     * Create alert http headers.
     *
     * @param message the message
     * @param param   the param
     * @return the http headers
     */
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-reduxlApp-alert", message);
        headers.add("X-reduxlApp-params", param);
        return headers;
    }

    /**
     * Create entity creation alert http headers.
     *
     * @param entityName the entity name
     * @param param      the param
     * @return the http headers
     */
    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("reduxlApp." + entityName + ".created", param);
    }

}
