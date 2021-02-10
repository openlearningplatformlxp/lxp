package com.redhat.uxl.webapp.web.filter;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

/**
 * The type Custom switch user filter.
 */
public class CustomSwitchUserFilter extends SwitchUserFilter {
    private static final String IMPERSONATE_PASSWORD_PARAMETER_NAME = "password";

    @Value("${app.security.impersonate.enabled}")
    private boolean appSecurityImpersonateEnabled;

    @Value("${app.security.impersonate.password}")
    private String appSecurityImpersonatePassword;

    @Value("${app.security.impersonate.passwordRequired}")
    private boolean appSecurityImpersonatePasswordRequired;

    @Override
    protected Authentication attemptSwitchUser(HttpServletRequest request) throws AuthenticationException {
        if (!appSecurityImpersonateEnabled) {
            throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
        }

        if (appSecurityImpersonatePasswordRequired) {
            String rawPassword = request.getParameter(IMPERSONATE_PASSWORD_PARAMETER_NAME);

            if (StrUtils.isBlank(rawPassword)) {
                throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
            }

            if (!BCrypt.checkpw(rawPassword, appSecurityImpersonatePassword)) {
                throw new GeneralException(ErrorCodeGeneral.FORBIDDEN);
            }
        }

        return super.attemptSwitchUser(request);
    }
}
