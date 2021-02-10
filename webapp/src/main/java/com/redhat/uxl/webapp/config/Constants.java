package com.redhat.uxl.webapp.config;

/**
 * The type Constants.
 */
public final class Constants {

    /**
     * The constant SPRING_PROFILE_DEVELOPMENT.
     */
    // Spring profile for development, production and "fast", see
    // http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    /**
     * The constant SPRING_PROFILE_PRODUCTION.
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    /**
     * The constant SPRING_PROFILE_FAST.
     */
    public static final String SPRING_PROFILE_FAST = "fast";
    /**
     * The constant SPRING_PROFILE_CLOUD.
     */
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";

    /**
     * The constant SYSTEM_ACCOUNT.
     */
    public static final String SYSTEM_ACCOUNT = "system";

    private Constants() {
    }
}
