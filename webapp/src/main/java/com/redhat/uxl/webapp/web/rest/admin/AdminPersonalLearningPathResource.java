package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.webapp.scheduler.PersonalLearningPathDueDatePassedJobService;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.SuccessDTO;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin personal learning path resource.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/admin/personal-learning-path", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminPersonalLearningPathResource extends BaseResource {
    @Inject
    private PersonalLearningPathDueDatePassedJobService personalLearningPathDueDatePassedJobService;

    /**
     * Recheck due dates success dto.
     *
     * @return the success dto
     */
    @ApiOperation(value = "Reindex Due Dates", notes = "<p>Recheck due dates to send notifications to users.</p>")
    @RequestMapping(value = "/recheck", method = RequestMethod.GET)
    @Timed
    public SuccessDTO recheckDueDates() {
        SuccessDTO successDTO = new SuccessDTO();
        personalLearningPathDueDatePassedJobService.checkDueDatePassed();
        successDTO.setSuccess(false);
        return successDTO;
    }

}
