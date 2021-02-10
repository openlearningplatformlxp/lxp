package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.SystemMutexService;
import com.redhat.uxl.webapp.scheduler.RemoveExpiredPasswordResetTokensSchedulerService;
import javax.inject.Inject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The type Remove expired password reset tokens scheduler service.
 */
@ConditionalOnProperty("app.passwordResetTokens.removeExpiredJob.enabled")
@Service
public class RemoveExpiredPasswordResetTokensSchedulerServiceImpl
        implements RemoveExpiredPasswordResetTokensSchedulerService {

    @Inject
    private PersonService personService;

    @Inject
    private SystemMutexService systemMutexService;

    @Override
    @Scheduled(cron = "${app.passwordResetTokens.removeExpiredJob.cronSchedule}")
    public void removeExpiredPasswordResetTokensScheduler() {
        boolean acquiredMutex = false;

        try {
            acquiredMutex = systemMutexService.acquire(SystemMutexType.REMOVE_EXPIRED_PASSWORD_RESET_TOKENS_JOB);

            if (acquiredMutex) {
                personService.removeExpiredPasswordResetTokens();
            }
        } finally {
            if (acquiredMutex) {
                systemMutexService.release(SystemMutexType.REMOVE_EXPIRED_PASSWORD_RESET_TOKENS_JOB);
            }
        }
    }
}
