package org.springframework.security.saml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.web.CustomSAMLBootstrap;

@Configuration
@EnableWebSecurity
public abstract class AppSAMLConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public static SAMLBootstrap SAMLBootstrap() {
        return new CustomSAMLBootstrap();
    }

}
