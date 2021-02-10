package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.datalayer.repository.PersistentTokenRepository;
import com.redhat.uxl.dataobjects.domain.PersistentToken;
import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.SystemMutexService;
import com.redhat.uxl.webapp.scheduler.RemoveOldPersistentTokensSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Remove old persistent tokens scheduler service.
 */
@ConditionalOnProperty("app.persistentTokens.removeOldJob.enabled")
@Service
@Slf4j
public class RemoveOldPersistentTokensSchedulerServiceImpl implements RemoveOldPersistentTokensSchedulerService {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private PersonService personService;

    @Inject
    private SystemMutexService systemMutexService;

    @Scheduled(cron = "${app.persistentTokens.removeOldJob.cronSchedule}")
    public void removeOldPersistentTokensScheduler() {
        boolean acquiredMutex = false;

        try {
            acquiredMutex = systemMutexService.acquire(SystemMutexType.REMOVE_OLD_PERSISTENT_TOKENS_JOB);

            if (acquiredMutex) {
                LocalDate now = new LocalDate();
                List<PersistentToken> tokens = persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1));
                for (PersistentToken token : tokens) {
                    try {
                        personService.removeOldPersistentToken(token.getSeries());
                    } catch (Exception e) {
                    }
                }
            }
        } finally {
            if (acquiredMutex) {
                systemMutexService.release(SystemMutexType.REMOVE_OLD_PERSISTENT_TOKENS_JOB);
            }
        }
    }
}
