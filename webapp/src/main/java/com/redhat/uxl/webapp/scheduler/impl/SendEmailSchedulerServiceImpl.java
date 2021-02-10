package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.commonjava.utils.ThreadUtils;
import com.redhat.uxl.datalayer.repository.EmailRepository;
import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import com.redhat.uxl.services.service.MailService;
import com.redhat.uxl.services.service.SystemMutexService;
import com.redhat.uxl.webapp.scheduler.SendEmailSchedulerService;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The type Send email scheduler service.
 */
@ConditionalOnProperty("app.mail.sendEmailJob.enabled")
@Service
public class SendEmailSchedulerServiceImpl implements SendEmailSchedulerService {

    @Value("${app.mail.sendEmailJob.maxAttempts}")
    private int appMailSendEmailJobMaxAttempts;

    @Value("${app.mail.sendEmailJob.sendDelayMS}")
    private long appMailSendEmailJobSendDelayMS;

    @Inject
    private EmailRepository emailRepository;

    @Inject
    private MailService mailService;

    @Inject
    private SystemMutexService systemMutexService;

    @Override
    @Scheduled(fixedDelayString = "${app.mail.sendEmailJob.taskDelayMS}", initialDelayString = "${app.mail.sendEmailJob.taskInitialDelayMS}")
    public void sendEmailScheduler() {
        boolean acquiredMutex = false;

        try {
            acquiredMutex = systemMutexService.acquire(SystemMutexType.SEND_EMAIL_JOB);

            if (acquiredMutex) {
                Email email = emailRepository.findTopBySentDateIsNullAndAttemptCountLessThanOrderByCreatedDateAsc(
                        appMailSendEmailJobMaxAttempts);
                Set<Long> failedEmailIdsSet = new HashSet<>();
                int mailCount = 0;

                while (email != null) {
                    try {
                        if (mailCount > 0) {
                            ThreadUtils.attemptToSleep(appMailSendEmailJobSendDelayMS);
                        }

                        mailCount++;

                        boolean sent = mailService.sendEmail(email.getId());

                        if (!sent) {
                            failedEmailIdsSet.add(email.getId());
                        }
                    } catch (Exception e) {
                        failedEmailIdsSet.add(email.getId());
                    }

                    if (failedEmailIdsSet.size() > 0) {
                        email = emailRepository
                                .findTopBySentDateIsNullAndAttemptCountLessThanAndIdNotInOrderByCreatedDateAsc(
                                        appMailSendEmailJobMaxAttempts, failedEmailIdsSet);
                    } else {
                        email = emailRepository.findTopBySentDateIsNullAndAttemptCountLessThanOrderByCreatedDateAsc(
                                appMailSendEmailJobMaxAttempts);
                    }
                }
            }
        } finally {
            if (acquiredMutex) {
                systemMutexService.release(SystemMutexType.SEND_EMAIL_JOB);
            }
        }
    }
}
