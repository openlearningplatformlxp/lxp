package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * The type Totara classes solr pager service.
 */
@Service
public class TotaraClassesSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<TotaraEventDTO, Serializable> {

  @Override
  protected Page<TotaraEventDTO> findActiveItems(Serializable data, int page, int maxSize) {
    return totaraCourseService.findActiveEvents(page, maxSize);
  }

  @Override
  protected List<CourseDocument> buildDocuments(Serializable data, List<TotaraEventDTO> content) {
    return totaraCourseService.buildCourseDocumentsFromEvent(content);
  }

  @Override
  protected int findTotalElements() {
    Long value = totaraCourseService.findActiveEvents(1, 1).getTotalElements();
    return value.intValue();
  }

  @Override
  public String logName() {
    return "Totara Classes";
  }
}
