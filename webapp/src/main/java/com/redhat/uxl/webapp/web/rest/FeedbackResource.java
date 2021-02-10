package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.FeedbackService;
import com.redhat.uxl.services.service.dto.FeedbackDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Feedback resource.
 */
@RestController
@RequestMapping(value = "/api/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FeedbackResource extends BaseResource {

    /**
     * The constant MAX_SIZE.
     */
    public static final int MAX_SIZE = 100;
    @Inject
    private FeedbackService feedbackService;

    /**
     * Save feedback event.
     *
     * @param feedbackDTO the feedback dto
     */
    @ApiOperation(value = "Save user's feedback", notes = "<p>Save user's feedback.</p>")
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public void saveFeedbackEvent(@RequestBody FeedbackDTO feedbackDTO) {
        log.debug("REST request to post feedback");

        feedbackService.saveFeedback(Long.valueOf(SecurityUtils.getCurrentLogin()), feedbackDTO);
    }

}
