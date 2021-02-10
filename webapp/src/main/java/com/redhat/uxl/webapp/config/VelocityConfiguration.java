package com.redhat.uxl.webapp.config;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * The type Velocity configuration.
 */
@Configuration
public class VelocityConfiguration {

    /**
     * Velocity engine velocity engine.
     *
     * @return the velocity engine
     * @throws Exception the exception
     */
    @Bean
    public VelocityEngine velocityEngine() throws Exception {
        // TODO: WJK: Determine if these are the right properties to use
        Properties properties = new Properties();
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        return velocityEngine;
    }
}
