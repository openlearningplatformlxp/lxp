package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.datalayer.repository.PersonActivationTokenRepository;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.SystemMutexService;
import com.redhat.uxl.webapp.scheduler.RemoveNotActivatedPersonSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Remove not activated person scheduler service.
 */
@ConditionalOnProperty("app.persons.removeNotActivatedJob.enabled")
@Service
@Slf4j
public class RemoveNotActivatedPersonSchedulerServiceImpl implements RemoveNotActivatedPersonSchedulerService {

    @Inject
    private PersonActivationTokenRepository personActivationTokenRepository;

    @Inject
    private PersonService personService;

    @Inject
    private SystemMutexService systemMutexService;

    @Scheduled(cron = "${app.persons.removeNotActivatedJob.cronSchedule}")
    public void removeNotActivatedPersonScheduler() {
        boolean acquiredMutex = false;

        try {
            acquiredMutex = systemMutexService.acquire(SystemMutexType.REMOVE_NOT_ACTIVATED_PERSONS_JOB);

            if (acquiredMutex) {
                List<PersonActivationToken> activationTokens = personActivationTokenRepository
                        .findAllByExpiryDateTimeBefore(DateTime.now());

                for (PersonActivationToken activationToken : activationTokens) {
                    try {
                        personService.removeNotActivatedPerson(activationToken.getId());
                    } catch (Exception e) {
                    }
                }
            }
        } finally {
            if (acquiredMutex) {
                systemMutexService.release(SystemMutexType.REMOVE_NOT_ACTIVATED_PERSONS_JOB);
            }
        }
    }
}
