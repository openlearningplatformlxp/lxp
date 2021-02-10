package com.redhat.uxl.webapp.scheduler;

/**
 * The interface Remove expired password reset tokens scheduler service.
 */
public interface RemoveExpiredPasswordResetTokensSchedulerService {
    /**
     * Remove expired password reset tokens scheduler.
     */
    void removeExpiredPasswordResetTokensScheduler();
}
