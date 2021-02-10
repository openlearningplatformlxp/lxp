package com.redhat.uxl.webapp.web.utils;

import com.redhat.uxl.commonjava.utils.StrUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * The type Request utils.
 */
public final class RequestUtils {
    private RequestUtils() {
    }

    /**
     * Gets base url.
     *
     * @param request the request
     * @return the base url
     */
    public static String getBaseUrl(HttpServletRequest request) {
        return getBaseUrl(request, true);
    }

    /**
     * Gets base url.
     *
     * @param request       the request
     * @param includeScheme the include scheme
     * @return the base url
     */
    public static String getBaseUrl(HttpServletRequest request, boolean includeScheme) {
        StringBuilder baseUrl = new StringBuilder();

        if (includeScheme) {
            baseUrl.append(request.getScheme()).append(":");
        }

        baseUrl.append("//").append(request.getServerName());

        if (request.getServerPort() != 80) {
            baseUrl.append(":").append(request.getServerPort());
        }

        if (StrUtils.isNotBlank(request.getContextPath())) {
            baseUrl.append(request.getContextPath());
        }

        return baseUrl.toString();
    }
}
