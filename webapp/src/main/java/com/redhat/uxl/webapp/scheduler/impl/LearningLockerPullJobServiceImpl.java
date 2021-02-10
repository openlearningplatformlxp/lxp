package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecution;
import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecutionDetail;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionDetailResultType;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import com.redhat.uxl.services.service.LearningLockerService;
import com.redhat.uxl.services.service.dto.LearningLockerEdgeDTO;
import com.redhat.uxl.services.service.dto.LearningLockerNodeDTO;
import com.redhat.uxl.services.service.dto.LearningLockerResponseDTO;
import com.redhat.uxl.services.service.dto.LearningLockerStatementObjectDTO;
import com.redhat.uxl.webapp.scheduler.LearningLockerPullJobService;
import lombok.extern.java.Log;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The type Learning locker pull job service.
 */
@Log
@ConditionalOnProperty("app.learninglocker.pullJob.enabled")
@Service
public class LearningLockerPullJobServiceImpl implements LearningLockerPullJobService {

    @Inject
    private LearningLockerService learningLockerService;

    @Override
    @Scheduled(cron = "${app.learninglocker.pullJob.cronSchedule}")
    public void pullLearningLockerData() {
        log.log(Level.INFO, "Pulling data from learning locker");
        pullData(LearningLockerJobExecutionType.KALTURA);
        pullData(LearningLockerJobExecutionType.LYNDA);
        pullData(LearningLockerJobExecutionType.ALLEGO);
    }

    private void pullData(LearningLockerJobExecutionType type) {
        LearningLockerJobExecution execution = new LearningLockerJobExecution();
        execution.setExecutionDate(DateTime.now());
        execution.setType(type);
        DateTime oldExecutionDate = learningLockerService.findLastJobExecution(type);
        List<LearningLockerJobExecutionDetail> details = new ArrayList<>();
        LearningLockerResponseDTO response = null;
        String cursor = null;
        do {
            response = learningLockerService.pullStatementsDataAfter(type, oldExecutionDate, cursor);
            cursor = response.getPageInfo().getEndCursor();
            List<LearningLockerEdgeDTO> edges = response.getEdges();
            execution.increaseActivitiesFound(edges.size());
            edges.stream().forEach(edge -> {
                LearningLockerJobExecutionDetail detail = new LearningLockerJobExecutionDetail();
                details.add(detail);
                LearningLockerNodeDTO node = edge.getNode();
                String email = node.getStatement().getActor().getEmail();
                String url = node.getStatement().getObject().getId();
                switch (type) {
                case ALLEGO:
                    // For some reason this is an array
                    if (node.getStatement() != null && node.getStatement().getContext() != null
                            && node.getStatement().getContext().getContextActivities() != null) {
                        List<LearningLockerStatementObjectDTO> parent = node.getStatement().getContext()
                                .getContextActivities().getParent();
                        if (parent != null && !parent.isEmpty()) {
                            url = parent.get(0).getId();
                        }
                        break;
                    }
                }
                detail.setVerb(node.getStatement().getVerb().getDisplay().getEn());
                detail.setTimestamp(node.getStatement().getTimestamp());
                try {
                    learningLockerService.completeActivity(type, email, url, detail);
                    execution.increaseActivitiesCompleted();
                    detail.setType(LearningLockerJobExecutionDetailResultType.SUCCESS);
                } catch (RuntimeException e) {
                    execution.increaseActivitiesCourseNotFound();
                    detail.setType(LearningLockerJobExecutionDetailResultType.FAILURE);
                }
            });
        } while (response.getPageInfo().getHasNextPage());
        learningLockerService.postJobExecution(execution, details);
    }
}
