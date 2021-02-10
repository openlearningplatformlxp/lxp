package com.redhat.uxl.webapp.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The type Security utils test.
 */
public class SecurityUtilsTest {

    /**
     * Test get current login.
     */
    @Test
    public void testGetCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);
        String login = SecurityUtils.getCurrentLogin();
        assertThat(login).isEqualTo("admin");
    }

    /**
     * Test is authenticated.
     */
    @Test
    public void testIsAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);
        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        assertThat(isAuthenticated).isTrue();
    }

    /**
     * Test anonymous is not authenticated.
     */
    @Test
    public void testAnonymousIsNotAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken("anonymousUser", "anonymousUser", authorities)); // TODO:
                                                                                                         // SAC:
                                                                                                         // this
                                                                                                         // effects
                                                                                                         // other
                                                                                                         // tests
                                                                                                         // (and
                                                                                                         // is
                                                                                                         // required
                                                                                                         // for
                                                                                                         // other
                                                                                                         // tests
                                                                                                         // to
                                                                                                         // function)
        SecurityContextHolder.setContext(securityContext);
        boolean isAuthenticated = SecurityUtils.isAuthenticated();
        assertThat(isAuthenticated).isFalse();
    }
}
