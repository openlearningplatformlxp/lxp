package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.repository.FeedbackRepository;
import com.redhat.uxl.dataobjects.domain.Feedback;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFeedbackDTO;
import com.redhat.uxl.dataobjects.domain.types.FeedbackType;
import com.redhat.uxl.services.service.FeedbackService;
import com.redhat.uxl.services.service.dto.FeedbackDTO;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * The type Feedback service.
 */
@Service
@Transactional
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

  @Inject
  private FeedbackRepository feedbackRepository;

  @Inject
  private TotaraProfileDAO totaraProfileDAO;

  @Override
  @Timed
  @Transactional
  public Page<Feedback> findForPagedSearch(String searchOperation, String searchValue,
      FeedbackType feedbackType, Pageable pageable) {
    SearchBuilder<Feedback> searchBuilder = new SearchBuilder<>(feedbackRepository);

    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("message", searchOperation, searchValue),
            SearchSpec.valueOf("email", searchOperation, searchValue),
            SearchSpec.valueOf("url", searchOperation, searchValue),
            SearchSpec.valueOf("businessLine", searchOperation, searchValue),
            SearchSpec.valueOf("jobTitle", searchOperation, searchValue),
            SearchSpec.valueOf("region", searchOperation, searchValue));

    if (feedbackType != null) {
      searchBuilder = searchBuilder.where("or",
          SearchSpec.equals("type", feedbackType.name())
      );
    }

    Page<Feedback> feedbackPage = searchBuilder.findForPagedSearch(pageable);

    return feedbackPage;
  }

  @Override
  public void saveFeedback(Long totaraId, FeedbackDTO feedbackDTO) {
    // get user profile field from totara
    TotaraFeedbackDTO totaraFeedbackDTO = totaraProfileDAO.getUserProfileFieldForFeedback(totaraId);

    Feedback feedback = new Feedback();
    feedback.setTotaraId(totaraId);
    feedback.setJobTitle(totaraFeedbackDTO.getLearnerJobTitle());
    feedback.setRegion(totaraFeedbackDTO.getRegion());
    feedback.setEmail(totaraFeedbackDTO.getEmail());
    feedback.setType(feedbackDTO.getType());
    feedback.setMessage(feedbackDTO.getMessage());
    feedback.setUrl(feedbackDTO.getUrl());
    feedback.setCreatedBy(totaraId.toString());
    feedback.setLastModifiedBy(totaraId.toString());
    feedbackRepository.save(feedback);
  }

  @Override
  public List<Feedback> getAllFeedback() {
    return feedbackRepository.findAll();
  }
}
