package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.Feedback;
import com.redhat.uxl.dataobjects.domain.types.FeedbackType;
import com.redhat.uxl.services.service.dto.FeedbackDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Feedback service.
 */
public interface FeedbackService {

    /**
     * Save feedback.
     *
     * @param totaraId    the totara id
     * @param feedbackDTO the feedback dto
     */
    void saveFeedback(Long totaraId, FeedbackDTO feedbackDTO);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param feedbackType    the feedback type
     * @param pageable        the pageable
     * @return the page
     */
    Page<Feedback> findForPagedSearch(String searchOperation, String searchValue, FeedbackType feedbackType,
            Pageable pageable);

    /**
     * Gets all feedback.
     *
     * @return the all feedback
     */
    List<Feedback> getAllFeedback();
}
