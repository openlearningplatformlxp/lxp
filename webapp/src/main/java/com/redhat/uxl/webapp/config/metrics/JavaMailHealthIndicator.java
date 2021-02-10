package com.redhat.uxl.webapp.config.metrics;

import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;

/**
 * The type Java mail health indicator.
 */
@Slf4j
public class JavaMailHealthIndicator extends AbstractHealthIndicator {
    private JavaMailSender javaMailSender;

    /**
     * Instantiates a new Java mail health indicator.
     *
     * @param javaMailSender the java mail sender
     */
    public JavaMailHealthIndicator(JavaMailSender javaMailSender) {
        Assert.notNull(javaMailSender, "javaMailSender must not be null");

        if (!(javaMailSender instanceof JavaMailSenderImpl)) {
            log.warn(
                    "JavaMailHealthIndicator instantiated with JavaMailSender that is not an instance of JavaMailSenderImpl... Health Status will not be monitored.");
        }

        this.javaMailSender = javaMailSender;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        log.debug("Initializing JavaMail health indicator");
        try {
            if (javaMailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;

                javaMailSenderImpl.getSession().getTransport().connect(javaMailSenderImpl.getHost(),
                        javaMailSenderImpl.getPort(), javaMailSenderImpl.getUsername(),
                        javaMailSenderImpl.getPassword());

                builder.up();
            } else {
                builder.unknown().withDetail("Reason",
                        "Health Indicator not implemented for " + javaMailSender.getClass().getName());
            }

        } catch (MessagingException e) {
            log.debug("Cannot connect to email server. Error: {}", e.getMessage());
            builder.down(e);
        }
    }
}
