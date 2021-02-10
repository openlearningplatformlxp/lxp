package com.redhat.uxl.webapp.config.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.webapp.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Logback login converter.
 */
public class LogbackLoginConverter extends ClassicConverter {
    private static final String USERNAME_PATTERN = "login=%s";
    private static final String USERNAME_REPLACE_TEXT = "%s";

    @Override
    public String convert(ILoggingEvent event) {
        String login = SecurityUtils.getCurrentLogin();

        return (StrUtils.isNotBlank(login) ? StringUtils.replace(USERNAME_PATTERN, USERNAME_REPLACE_TEXT, login) : "-");
    }
}
