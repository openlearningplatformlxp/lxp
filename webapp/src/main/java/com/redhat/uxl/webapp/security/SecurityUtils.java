package com.redhat.uxl.webapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The type Security utils.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Gets current login as long.
     *
     * @return the current login as long
     */
    public static Long getCurrentLoginAsLong() {
        String login = getCurrentLogin();
        if (login != null) {
            return Long.valueOf(login);
        } else {
            return 0L;
        }
    }

    /**
     * Gets current login.
     *
     * @return the current login
     */
    public static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser = null;
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    /**
     * Is authenticated boolean.
     *
     * @return the boolean
     */
    public static boolean isAuthenticated() {
        return !hasGrantedAuthority(AuthoritiesConstants.ANONYMOUS);
    }

    /**
     * Is impersonated user boolean.
     *
     * @return the boolean
     */
    public static boolean isImpersonatedUser() {
        return hasGrantedAuthority(AuthoritiesConstants.GRANTED_PREVIOUS_ADMINISTRATOR);
    }

    /**
     * Has granted authority boolean.
     *
     * @param role the role
     * @return the boolean
     */
    public static boolean hasGrantedAuthority(String role) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();

        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }

        return false;
    }
}
