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
import org.springframework.context.annotation.ImportResource;
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
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CsrfFilter;

import javax.inject.Inject;

/**
 * The type Saml security configuration.
 */
@ConditionalOnProperty("saml.redirect.enabled")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@ImportResource(value = "classpath:/security/securityContext.xml")
public class SAMLSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${app.security.csrf.enabled}")
    private boolean appSecurityCsrfEnabled;

    @Value("${app.security.impersonate.enabled}")
    private boolean appSecurityImpersonateEnabled;

    @Value("${app.security.impersonate.targetURL}")
    private String appSecurityImpersonateTargetURL;

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

    @Inject
    private SAMLAuthenticationProvider samlAuthenticationProvider;

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        auth.authenticationProvider(samlAuthenticationProvider);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**").antMatchers("/bower_components/**").antMatchers("/i18n/**")
                .antMatchers("/scripts/**/*.{js,html}").antMatchers("/swagger-ui/index.html").antMatchers("/test/**")
                .antMatchers("/vendor/**");
    }

    /**
     * Saml entry point saml entry point.
     *
     * @return the saml entry point
     */
    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
        samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());

        return samlEntryPoint;
    }

    /**
     * Default web sso profile options web sso profile options.
     *
     * @return the web sso profile options
     */
    @Bean
    public WebSSOProfileOptions defaultWebSSOProfileOptions() {
        WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
        webSSOProfileOptions.setIncludeScoping(false);
        return webSSOProfileOptions;
    }

    /**
     * Success logout handler simple url logout success handler.
     *
     * @return the simple url logout success handler
     */
    @Bean
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        simpleUrlLogoutSuccessHandler.setDefaultTargetUrl("/");
        simpleUrlLogoutSuccessHandler.setAlwaysUseDefaultTargetUrl(true);

        return simpleUrlLogoutSuccessHandler;
    }

    /**
     * Logout handler security context logout handler.
     *
     * @return the security context logout handler
     */
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);
        return logoutHandler;
    }

    /**
     * Saml logout filter saml logout filter.
     *
     * @return the saml logout filter
     */
    @Bean
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[] { logoutHandler() },
                new LogoutHandler[] { logoutHandler() });
    }

    /**
     * Saml logout processing filter saml logout processing filter.
     *
     * @return the saml logout processing filter
     */
    @Bean
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
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

        http.csrf().disable();

        http.exceptionHandling()

                .authenticationEntryPoint(samlEntryPoint())
                .and().rememberMe().rememberMeServices(rememberMeServices).rememberMeParameter("remember-me")
                .key(env.getProperty("jhipster.security.rememberme.key")).and().formLogin()
                .loginProcessingUrl("/api/authentication").successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler).usernameParameter("j_username")
                .passwordParameter("j_password").permitAll().and().logout()
                .logoutUrl("/api/logout").logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .deleteCookies("JSESSIONID", "CSRF-TOKEN").permitAll().and().headers().frameOptions().disable()
                .and().authorizeRequests().antMatchers("/api/account/page/register").permitAll().antMatchers("/saml/**")
                .permitAll().antMatchers("/logout/**").permitAll().antMatchers("/logout").permitAll()
                .antMatchers("/api/account/register").permitAll().antMatchers("/api/account/activate").permitAll()
                .antMatchers("/api/account/authenticate").permitAll().antMatchers("/api/account/reset_password/init")
                .permitAll().antMatchers("/api/account/reset_password/finish").permitAll().antMatchers("/api/logs/**")
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
                .antMatchers("/protected/**").authenticated().anyRequest().authenticated();

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
}
