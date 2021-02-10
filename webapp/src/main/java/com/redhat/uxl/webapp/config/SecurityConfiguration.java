package com.redhat.uxl.webapp.config;

import com.redhat.uxl.webapp.security.AjaxAuthenticationFailureHandler;
import com.redhat.uxl.webapp.security.AjaxAuthenticationSuccessHandler;
import com.redhat.uxl.webapp.security.AjaxLogoutSuccessHandler;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.Http401UnauthorizedEntryPoint;
import com.redhat.uxl.webapp.web.filter.CsrfCookieGeneratorFilter;
import com.redhat.uxl.webapp.web.filter.CustomSwitchUserFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.inject.Inject;

/**
 * The type Security configuration.
 */
@ConditionalOnProperty(value = "saml.redirect.enabled", havingValue = "false")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${app.security.csrf.enabled}")
    private boolean appSecurityCsrfEnabled;

    @Value("${app.security.impersonate.enabled}")
    private boolean appSecurityImpersonateEnabled;

    @Value("${app.security.impersonate.targetURL}")
    private String appSecurityImpersonateTargetURL;

    @Value("${app.security.client.session.cookie.domainNamePattern}")
    private String appSecuritySessionCookieDomainNamePattern;

    @Value("${app.security.client.session.cookie.httpOnly}")
    private boolean appSecuritySessionCookieHttpOnly;

    @Value("${app.security.client.session.cookie.name}")
    private String appSecuritySessionCookieName;

    @Value("${app.security.client.session.cookie.path}")
    private String appSecuritySessionCookiePath;

    @Value("${app.security.client.session.cookie.secure}")
    private boolean appSecuritySessionCookieSecure;

    @Inject
    private Environment env;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**").antMatchers("/bower_components/**").antMatchers("/i18n/**")
                .antMatchers("/scripts/**/*.{js,html}").antMatchers("/swagger-ui/index.html").antMatchers("/test/**")
                .antMatchers("/vendor/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (appSecurityCsrfEnabled) {
            http.addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class);

            http.csrf().ignoringAntMatchers("/websocket/**");
        } else {
            http.csrf().disable();
        }

        if (appSecurityImpersonateEnabled) {
            http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
        }

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().rememberMe()
                .rememberMeServices(rememberMeServices).rememberMeParameter("remember-me")
                .key(env.getProperty("jhipster.security.rememberme.key")).and().formLogin()
                .loginProcessingUrl("/api/authentication").successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler).usernameParameter("j_username")
                .passwordParameter("j_password").permitAll().and().logout().logoutUrl("/api/logout")
                .deleteCookies("JSESSIONID", "CSRF-TOKEN").logoutSuccessHandler(ajaxLogoutSuccessHandler).permitAll()
                .and().headers().frameOptions().disable().and().authorizeRequests()
                .antMatchers("/api/account/page/register").permitAll().antMatchers("/saml/login").permitAll()
                .antMatchers("/login").permitAll().antMatchers("/api/account/register").permitAll()
                .antMatchers("/api/account/activate").permitAll().antMatchers("/api/account/authenticate").permitAll()
                .antMatchers("/api/account/reset_password/init").permitAll()
                .antMatchers("/api/account/reset_password/finish").permitAll().antMatchers("/api/logs/**")
                .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/audits/**")
                .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/system/bootstrap/**").permitAll()
                .antMatchers("/api/system/health-check").permitAll().antMatchers("/api/system/info").permitAll()
                .antMatchers("/api/course/**").permitAll().antMatchers("/api/message/**").permitAll()
                .antMatchers("/api/course-enrollment/**").permitAll().antMatchers("/api/**").authenticated()
                .antMatchers("/admin/manage/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/mappings/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/manage/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/impersonate/start").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/admin/impersonate/stop").authenticated().antMatchers("/mappings/**")
                .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/configuration/security").permitAll().antMatchers("/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/protected/**").authenticated();

    }

    /**
     * Security evaluation context extension security evaluation context extension.
     *
     * @return the security evaluation context extension
     */
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    /**
     * Switch user filter switch user filter.
     *
     * @return the switch user filter
     */
    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new CustomSwitchUserFilter();

        filter.setUserDetailsService(userDetailsService);
        filter.setSwitchUserUrl("/impersonate/start");
        filter.setExitUserUrl("/impersonate/stop");
        filter.setTargetUrl(appSecurityImpersonateTargetURL);

        return filter;
    }

    /**
     * https://docs.spring.io/spring-session/docs/2.0.0.RELEASE/reference/html5/guides/java-custom-cookie.html#custom-cookie-sample
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        serializer.setCookieName(appSecuritySessionCookieName);
        serializer.setCookiePath(appSecuritySessionCookiePath);
        serializer.setDomainNamePattern(appSecuritySessionCookieDomainNamePattern);
        serializer.setUseHttpOnlyCookie(appSecuritySessionCookieHttpOnly);
        serializer.setUseSecureCookie(appSecuritySessionCookieSecure);

        return serializer;
    }
}
