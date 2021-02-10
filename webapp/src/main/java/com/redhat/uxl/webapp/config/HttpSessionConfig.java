package com.redhat.uxl.webapp.config;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * The type Http session config.
 */
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800)
public class HttpSessionConfig {
}
