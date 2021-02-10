package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.webapp.scheduler.LearningLockerPullJobService;
import com.redhat.uxl.webapp.web.rest.dto.SuccessDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * The type Lrs resource.
 */
@RestController
@RequestMapping(value = "/api/lrs", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LRSResource extends BaseResource {

    /**
     * The constant MAX_SIZE.
     */
    public static final int MAX_SIZE = 10;
    @Inject
    private LearningLockerPullJobService learningLockerPullJobService;

    /**
     * Reindex success dto.
     *
     * @return the success dto
     */
    @ApiOperation(value = "Process LRS Feed ", notes = "<p>Process LRS Feed manually</p>")
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    @Timed
    public SuccessDTO reindex() {
        log.debug("REST request to process LRS");
        learningLockerPullJobService.pullLearningLockerData();
        SuccessDTO successDTO = new SuccessDTO();
        return successDTO;
    }

}
