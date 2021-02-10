package com.redhat.uxl.webapp.security;

import com.redhat.uxl.webapp.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * The type Spring security auditor aware.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentLogin();
        return Optional.of((userName != null ? userName : Constants.SYSTEM_ACCOUNT));
    }
}
