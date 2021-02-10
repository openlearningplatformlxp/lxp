package com.redhat.uxl.webapp.security;

import com.redhat.uxl.commonjava.utils.StrUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * The type Ajax authentication success handler.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);

        String successUrl = request.getParameter("successUrl");

        if (StrUtils.isNotBlank(successUrl)) {
            response.sendRedirect(successUrl);
        }
    }
}
