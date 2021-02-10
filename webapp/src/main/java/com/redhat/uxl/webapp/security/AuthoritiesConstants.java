package com.redhat.uxl.webapp.security;

/**
 * The type Authorities constants.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    /**
     * The constant ADMIN.
     */
    public static final String ADMIN = "ROLE_ADMIN";
    /**
     * The constant ANONYMOUS.
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    /**
     * The constant CMS_EDITOR.
     */
    public static final String CMS_EDITOR = "ROLE_CMS_EDITOR";
    /**
     * The constant WIKI_EDITOR.
     */
    public static final String WIKI_EDITOR = "ROLE_WIKI_EDITOR";
    /**
     * The constant USER.
     */
    public static final String USER = "ROLE_USER";

    /**
     * The constant SYSTEM_ROLES.
     */
    public static final String[] SYSTEM_ROLES = { ADMIN, ANONYMOUS, CMS_EDITOR, WIKI_EDITOR, USER };

    /**
     * The constant GRANTED_PREVIOUS_ADMINISTRATOR.
     */
    public static final String GRANTED_PREVIOUS_ADMINISTRATOR = "ROLE_PREVIOUS_ADMINISTRATOR"; // A
                                                                                               // granted
                                                                                               // role
                                                                                               // when
                                                                                               // an
                                                                                               // Admin
                                                                                               // is
                                                                                               // impersonating
                                                                                               // another
                                                                                               // user.
}
