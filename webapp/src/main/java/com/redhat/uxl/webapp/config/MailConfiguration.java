package com.redhat.uxl.webapp.config;

import com.redhat.uxl.commonjava.utils.StrUtils;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

/**
 * The type Mail configuration.
 */
@Configuration
@Slf4j
public class MailConfiguration {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String PROP_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String PROP_TRANSPORT_PROTO = "mail.transport.protocol";

    /**
     * Instantiates a new Mail configuration.
     */
    public MailConfiguration() {
    }
    @Value("${mail.host:localhost}")
    private String host;

    @Value("${mail.port:0}")
    private int port;

    @Value("${mail.username}")
    private String user;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.tls:false}")
    private Boolean tls;

    @Value("${mail.auth:false}")
    private Boolean auth;

    /**
     * Java mail sender java mail sender.
     *
     * @return the java mail sender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        log.debug("Configuring mail server");

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (StrUtils.isNotBlank(host)) {
            sender.setHost(host);
        } else {
            log.warn("Warning! Your SMTP server is not configured. We will try to use one on localhost.");
            log.debug("Did you configure your SMTP settings in your application.yml?");
            sender.setHost(DEFAULT_HOST);
        }
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);

        Properties sendProperties = new Properties();
        sendProperties.setProperty(PROP_SMTP_AUTH, auth.toString());
        sendProperties.setProperty(PROP_STARTTLS, tls.toString());
        sendProperties.setProperty(PROP_TRANSPORT_PROTO, protocol);
        sender.setJavaMailProperties(sendProperties);
        return sender;
    }
}
