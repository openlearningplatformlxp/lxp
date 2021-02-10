package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * The type Totara course solr pager service.
 */
@Service
public class TotaraCourseSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<TotaraCourseDTO, Serializable> {

  @Override
  protected Page<TotaraCourseDTO> findActiveItems(Serializable data, int page, int maxSize) {

    return totaraCourseService.findActiveCourses(page, maxSize);
  }

  @Override
  protected List<CourseDocument> buildDocuments(Serializable data, List<TotaraCourseDTO> content) {

    return totaraCourseService.buildCourseDocuments(content);
  }

  @Override
  protected int findTotalElements() {
    Long value = totaraCourseService.findActiveCourses(1, 1).getTotalElements();
    return value.intValue();
  }

  @Override
  public String logName() {
    return "Totara Course";
  }
}
