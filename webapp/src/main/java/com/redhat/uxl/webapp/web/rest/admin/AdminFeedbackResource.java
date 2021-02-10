package com.redhat.uxl.webapp.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Feedback;
import com.redhat.uxl.dataobjects.domain.types.FeedbackType;
import com.redhat.uxl.services.service.FeedbackService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.BaseResource;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Admin feedback resource.
 */
@RestController
@RequestMapping(value = "/api/admin/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminFeedbackResource extends BaseResource {
    @Inject
    private FeedbackService feedbackService;

    /**
     * Search feedbacks page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for Feedback.", notes = "<p>Search for Feedback.</p>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<Feedback> searchFeedbacks(@RequestBody PagedSearchDTO pagedSearchDTO) {

        String type = pagedSearchDTO.getOption("type");
        FeedbackType feedbackType = null;
        if (StringUtils.isNotEmpty(type)) {
            feedbackType = FeedbackType.valueOf(type);
        }

        return feedbackService.findForPagedSearch(pagedSearchDTO.getSearchOperation(), pagedSearchDTO.getSearchValue(),
                feedbackType, pagedSearchDTO.getPageable());
    }
}
