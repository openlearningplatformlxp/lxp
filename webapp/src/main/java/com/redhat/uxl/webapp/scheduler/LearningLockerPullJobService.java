package com.redhat.uxl.webapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * The interface Learning locker pull job service.
 */
public interface LearningLockerPullJobService {
    /**
     * Pull learning locker data.
     */
    @Scheduled(cron = "${app.passwordResetTokens.removeExpiredJob.cronSchedule}")
    void pullLearningLockerData();
}
