package com.redhat.uxl.webapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * The interface Personal learning path due date passed job service.
 */
public interface PersonalLearningPathDueDatePassedJobService {
    /**
     * Check due date passed.
     */
    @Scheduled(cron = "${app.path.personal.duedate.passed.cronSchedule}")
    void checkDueDatePassed();
}
