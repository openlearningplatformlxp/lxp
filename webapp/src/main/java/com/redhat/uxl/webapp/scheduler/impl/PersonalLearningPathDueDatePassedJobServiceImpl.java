package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.datalayer.dao.PersonalPlansDAO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.services.constants.MessageConstants;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.webapp.scheduler.PersonalLearningPathDueDatePassedJobService;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;

/**
 * The type Personal learning path due date passed job service.
 */
@Log
@ConditionalOnProperty("app.path.personal.duedate.passed.enabled")
@Service
public class PersonalLearningPathDueDatePassedJobServiceImpl implements PersonalLearningPathDueDatePassedJobService {

    /**
     * The Personal plans dao.
     */
    @Inject
    PersonalPlansDAO personalPlansDAO;
    /**
     * The Message service.
     */
    @Inject
    MessageService messageService;

    @Override
    @Scheduled(cron = "${app.path.personal.duedate.passed.cronSchedule}")
    public void checkDueDatePassed() {
        log.log(Level.INFO, "Starting job to check if there are personal path with due dates");
        List<PersonalProgramDTO> personalProgramDTOS = personalPlansDAO.getPlansWithDueDatePassed();
        personalProgramDTOS.forEach(pp -> {
            String messageText = MessageConstants.S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED;
            messageService.sendDueDateNotificationToUser(MessageConstants.S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED_TITLE,
                    messageText, pp.getUserId(), pp.getProgramId());
        });
    }

}
