package com.redhat.uxl.webapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * The type Saml deep link success handler.
 */
public class SAMLDeepLinkSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        HttpSession session = request.getSession(false);
        Enumeration sNames = session.getAttributeNames();
        while (sNames.hasMoreElements()) {
            String nameVar = (String) sNames.nextElement();
        }

        Collection<String> headers = savedRequest.getHeaderNames();

        List<String> targetUrlArray = savedRequest.getHeaderValues("Original-URI");
        String targetUrl = savedRequest.getRedirectUrl();
        for (String data : targetUrlArray) {
            if (data.indexOf(",") > 0) {
                targetUrl = data.substring(0, data.indexOf(","));
            } else {
                targetUrl = data;
            }
            break; // only need first element
        }

        clearAuthenticationAttributes(request);

        if (targetUrl != null && !targetUrl.contains("#")) {

            String newUrl = "https://" + request.getServerName() + "/#" + targetUrl;

            targetUrl = newUrl;


        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
