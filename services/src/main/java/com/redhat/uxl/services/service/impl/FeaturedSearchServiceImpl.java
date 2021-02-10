package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.repository.FeaturedSearchInstanceRepository;
import com.redhat.uxl.datalayer.repository.FeaturedSearchRepository;
import com.redhat.uxl.dataobjects.domain.FeaturedSearch;
import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.FeaturedSearchService;
import com.redhat.uxl.services.service.WikipageService;
import com.redhat.uxl.services.service.dto.FeaturedSearchDTO;
import com.redhat.uxl.services.service.dto.FeaturedSearchInstanceDTO;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.helper.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The type Featured search service.
 */
@Service
@Transactional
@Slf4j
public class FeaturedSearchServiceImpl implements FeaturedSearchService {

  @Inject
  private FeaturedSearchRepository featuredSearchRepository;
  @Inject
  private FeaturedSearchInstanceRepository featuredSearchInstanceRepository;
  @Inject
  private WikipageService wikipageService;
  @Inject
  private TotaraCourseDAO courseDAO;

  @Override
  @Timed
  @Transactional
  public Page<FeaturedSearchDTO> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<FeaturedSearch> searchBuilder = new SearchBuilder<>(featuredSearchRepository);

    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("keywords", searchOperation, searchValue));

    Page<FeaturedSearch> page = searchBuilder.findForPagedSearch(pageable);
    // To avoid unsorting first I create sequentially the objects
    List<FeaturedSearchDTO> dtos = page.getContent().stream().map(fs -> {
      FeaturedSearchDTO dto = new FeaturedSearchDTO();
      dto.setFeaturedSearchId(fs.getId());
      dto.setKeywords(fs.getKeywords());
      dto.setCreatedDate(fs.getCreatedDate());
      return dto;
    }).collect(Collectors.toList());
    // Then I find all required data in parallel
    dtos.parallelStream().forEach(fs -> {
      findFeaturedSearchInstances(fs);
    });
    Page<FeaturedSearchDTO> response = new PageImpl<>(dtos, pageable, page.getTotalElements());
    return response;
  }

  @Override
  public FeaturedSearchDTO findFeaturedSearchById(Long featuredSearchId) {
    Validate.notNull(featuredSearchId);
    FeaturedSearch fs = featuredSearchRepository.findOne(featuredSearchId);
    FeaturedSearchDTO dto = new FeaturedSearchDTO();
    dto.setFeaturedSearchId(fs.getId());
    dto.setKeywords(fs.getKeywords());
    findFeaturedSearchInstances(dto);
    return dto;
  }

  @Override
  public FeaturedSearchDTO createFeaturedSearch(Long userId, FeaturedSearchDTO dto) {
    Validate.notNull(userId);
    Validate.notNull(dto);
    final FeaturedSearch fs = new FeaturedSearch();
    fs.setCreatedBy(String.valueOf(userId));
    fs.setCreatedDate(new DateTime());
    fs.setKeywords(StringUtils.lowerCase(dto.getKeywords()));
    featuredSearchRepository.save(fs);
    dto.setFeaturedSearchId(fs.getId());
    List<FeaturedSearchInstance> instances = new ArrayList<>();
    dto.getInstances().stream().forEach(fsid -> {
      FeaturedSearchInstance fsi = new FeaturedSearchInstance();
      fsi.setFeaturedSearchId(fs.getId());
      fsi.setInstanceType(ProgramType.valueOf(fsid.getInstanceType()));
      fsi.setInstanceId(fsid.getInstanceId());
      fsi.setCreatedBy(String.valueOf(userId));
      fsi.setCreatedDate(new DateTime());
      instances.add(fsi);
    });

    featuredSearchInstanceRepository.save(instances);
    return dto;
  }

  @Override
  public FeaturedSearchDTO updateFeaturedSearch(Long userId, FeaturedSearchDTO dto) {
    Validate.notNull(userId);
    Validate.notNull(dto);
    FeaturedSearch storedFs = featuredSearchRepository.findOne(dto.getFeaturedSearchId());
    storedFs.setKeywords(StringUtils.lowerCase(dto.getKeywords()));
    storedFs.setLastModifiedBy(String.valueOf(userId));
    storedFs.setLastModifiedDate(new DateTime());
    featuredSearchRepository.save(storedFs);
    // First find stored ids to remove the ones that are not anymore in the list
    List<FeaturedSearchInstance> storedInstances =
        featuredSearchInstanceRepository.findByFeaturedSearchId(dto.getFeaturedSearchId());
    List<FeaturedSearchInstance> toDelete =
        storedInstances.stream().filter(i -> !dto.containsInstance(i)).collect(Collectors.toList());
    storedInstances.removeAll(toDelete);
    featuredSearchInstanceRepository.delete(toDelete);
    // Create the new ones
    List<FeaturedSearchInstance> newChildInstances = new ArrayList<>();
    dto.getInstances().stream().forEach(fsid -> {
      boolean stored = false;
      for (FeaturedSearchInstance storedFsid : storedInstances) {
        if (storedFsid.getInstanceId().equals(fsid.getInstanceId())) {
          stored = true;
          break;
        }
      }
      if (!stored) {
        FeaturedSearchInstance fsi = new FeaturedSearchInstance();
        fsi.setFeaturedSearchId(storedFs.getId());
        fsi.setInstanceType(ProgramType.valueOf(fsid.getInstanceType()));
        fsi.setInstanceId(fsid.getInstanceId());
        fsi.setCreatedBy(String.valueOf(userId));
        fsi.setCreatedDate(new DateTime());
        newChildInstances.add(fsi);
      }
    });
    featuredSearchInstanceRepository.save(newChildInstances);
    return dto;
  }

    @Override
    public void deleteFeatureSearch(Long featuredSearchId) {
        Validate.notNull(featuredSearchId);
        featuredSearchRepository.deleteById(featuredSearchId);
        List<FeaturedSearchInstance> storedInstances = featuredSearchInstanceRepository.findByFeaturedSearchId(featuredSearchId);
        featuredSearchInstanceRepository.delete(storedInstances);
    }

  @Override
  public Set<FeaturedSearchInstance> findFeaturedSearchByQuery(String search) {
    Validate.notNull(search);
    search = StringUtils.lowerCase(search);
    Set<FeaturedSearchInstance> set = new TreeSet<>();
    if (StringUtils.isNotEmpty(search)) {
      List<FeaturedSearch> featuredSearches =
          featuredSearchRepository.findFeaturedSearchesContaining(search);
      featuredSearches.forEach(fs -> {
        List<FeaturedSearchInstance> instances =
            featuredSearchInstanceRepository.findByFeaturedSearchId(fs.getId());
        instances.forEach(i -> {
          set.add(i);
        });
      });
    }
    return set;
  }

  private void findFeaturedSearchInstances(FeaturedSearchDTO fs) {
    Validate.notNull(fs);
    List<FeaturedSearchInstance> fsi =
        featuredSearchInstanceRepository.findByFeaturedSearchId(fs.getFeaturedSearchId());
    fs.setInstances(fsi.parallelStream().map(e -> {
      FeaturedSearchInstanceDTO childDto = new FeaturedSearchInstanceDTO();
      childDto.setInstanceId(e.getInstanceId());
      childDto.setInstanceType(e.getInstanceType().name());
      switch (e.getInstanceType()) {
        case COURSE:
          TotaraCourseDTO course = courseDAO.findCourseByCourseId(e.getInstanceId());
          if (course != null) {
            childDto.setInstanceName(course.getFullName());
          }
          break;
        case LEARNING_PATH:
          TotaraProgramDTO program = courseDAO.findProgramByProgramId(e.getInstanceId());
          if (program != null) {
            childDto.setInstanceName(program.getProgramName());
          }
          break;
        case WIKIPAGE:
          Wikipage wikipage = wikipageService.findPageById(e.getInstanceId());
          if (wikipage != null) {
            childDto.setInstanceName(wikipage.getTitle());
          }
          break;
      }
      return childDto;
    }).sorted().collect(Collectors.toList()));
  }

}
