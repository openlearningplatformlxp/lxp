package com.redhat.uxl.webapp.config.metrics;

import javax.inject.Inject;
import javax.sql.DataSource;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * The type J hipster health indicator configuration.
 */
@Configuration
public class JHipsterHealthIndicatorConfiguration {

    @Inject
    private JavaMailSender javaMailSender;

    @Inject
    private DataSource dataSource;

    /**
     * Db health indicator health indicator.
     *
     * @return the health indicator
     */
    @Bean
    public HealthIndicator dbHealthIndicator() {
        return new DatabaseHealthIndicator(dataSource);
    }

    /**
     * Mail health indicator health indicator.
     *
     * @return the health indicator
     */
    @Bean
    public HealthIndicator mailHealthIndicator() {
        return new JavaMailHealthIndicator(javaMailSender);
    }
}
