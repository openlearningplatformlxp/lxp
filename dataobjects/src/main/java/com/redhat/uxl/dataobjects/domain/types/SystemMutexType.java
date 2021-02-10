package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum System mutex type.
 */
public enum SystemMutexType {
    /**
     * Debug system mutex type.
     */
    DEBUG,
    /**
     * Remove expired password reset tokens job system mutex type.
     */
    REMOVE_EXPIRED_PASSWORD_RESET_TOKENS_JOB,
    /**
     * Remove not activated persons job system mutex type.
     */
    REMOVE_NOT_ACTIVATED_PERSONS_JOB,
    /**
     * Remove old persistent tokens job system mutex type.
     */
    REMOVE_OLD_PERSISTENT_TOKENS_JOB,
    /**
     * Send email job system mutex type.
     */
    SEND_EMAIL_JOB
}
