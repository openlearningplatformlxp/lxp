package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.services.service.dto.WikipageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Wikipages solr pager service.
 */
@Service
public class WikipagesSolrPagerServiceImpl
    extends BaseSolrPagerServiceImpl<Wikipage, Serializable> {

  @Override
  protected Page<Wikipage> findActiveItems(Serializable data, int page, int maxSize) {

        return wikipageRepository.findPublishedAndIndexedWikipages(PageRequest.of(page,maxSize));
    }

  @Override
  protected List<CourseDocument> buildDocuments(Serializable data, List<Wikipage> wikipages) {

    return wikipages.parallelStream().map((wikipage) -> {
      CourseDocument courseDocument = new CourseDocument(wikipage);
      WikipageDTO dto = wikipageService.findPageByIdWithTags(wikipage.getId());
      courseDocument
          .setTags(dto.getTags().stream().map(TagDTO::getName).collect(Collectors.toList()));
      return courseDocument;
    }).collect(Collectors.toList());
  }

  @Override
  protected int findTotalElements() {
    Long value = wikipageRepository.countPublishedAndIndexedWikipages();
    return value.intValue();
  }

  @Override
  public String logName() {
    return "Wikipage";
  }
}
