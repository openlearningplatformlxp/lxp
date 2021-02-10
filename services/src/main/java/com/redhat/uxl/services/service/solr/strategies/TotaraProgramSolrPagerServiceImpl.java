package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * The type Totara program solr pager service.
 */
@Service
public class TotaraProgramSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<TotaraProgramDTO, Serializable> {

  @Override
  protected Page<TotaraProgramDTO> findActiveItems(Serializable data, int page, int maxSize) {
    return totaraCourseService.findActivePrograms(page, maxSize);
  }

  @Override
  protected List<CourseDocument> buildDocuments(Serializable data, List<TotaraProgramDTO> content) {
    return totaraCourseService.buildCourseDocumentsFromProgram(content);
  }

  @Override
  protected int findTotalElements() {
    Long value = totaraCourseService.findActivePrograms(1, 1).getTotalElements();
    return value.intValue();
  }

  @Override
  public String logName() {
    return "Totara Program";
  }
}
