package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.services.service.TagsService;
import com.redhat.uxl.services.service.dto.SearchFilterDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Tags service.
 */
@Service
@Slf4j
@Transactional
public class TagsServiceImpl implements TagsService {

    /**
     * The Totara tag dao.
     */
    @Inject
  TotaraTagDAO totaraTagDAO;

  @Override
  @Timed
  @Transactional(readOnly = true)
  public SearchFilterDTO getSearchFilterValues() {
    SearchFilterDTO dto = new SearchFilterDTO();

    dto.setSkillLevels(
        totaraTagDAO.findSkillLevelTags().stream().map(new Function<TotaraTagDTO, TagDTO>() {
          @Override
          public TagDTO apply(TotaraTagDTO totaraTagDTO) {
            TagDTO filter = new TagDTO();
            filter.setId(totaraTagDTO.getId());
            filter.setName(totaraTagDTO.getName());
            return filter;
          }
        }).collect(Collectors.toList()));

    dto.setLanguages(
        totaraTagDAO.findLanguageTags().stream().map(new Function<TotaraTagDTO, TagDTO>() {
          @Override
          public TagDTO apply(TotaraTagDTO totaraTagDTO) {
            TagDTO filter = new TagDTO();
            filter.setId(totaraTagDTO.getId());
            filter.setName(totaraTagDTO.getName());
            return filter;
          }
        }).collect(Collectors.toList()));

    dto.setDeliveries(new ArrayList<>());
    dto.getDeliveries().add(new TagDTO(0l, "E-Learning"));
    dto.getDeliveries().add(new TagDTO(1l, "Blended"));
    dto.getDeliveries().add(new TagDTO(2l, "Face to face"));

    return dto;
  }

  @Override
  public List<TagDTO> findParentTags() {
    return totaraTagDAO.findParentTags().stream().map(new Function<TotaraTagDTO, TagDTO>() {
      @Override
      public TagDTO apply(TotaraTagDTO totaraTagDTO) {
        TagDTO filter = new TagDTO();
        filter.setId(totaraTagDTO.getId());
        filter.setName(totaraTagDTO.getName());
        return filter;
      }
    }).collect(Collectors.toList());
  }

  @Override
  public List<TagDTO> findChildTags(Long tagId) {
    return totaraTagDAO.findChildTags(tagId).stream().map(new Function<TotaraTagDTO, TagDTO>() {
      @Override
      public TagDTO apply(TotaraTagDTO totaraTagDTO) {
        TagDTO filter = new TagDTO();
        filter.setId(totaraTagDTO.getId());
        filter.setName(totaraTagDTO.getName());
        return filter;
      }
    }).collect(Collectors.toList());
  }

  @Override
  public TagDTO findTag(Long tagId) {
    TotaraTagDTO tag = totaraTagDAO.findTag(tagId);
    if (tag != null) {
      return new TagDTO(tag.getId(), tag.getName());
    } else {
      return null;
    }
  }

  @Override
  public TagDTO findParentTag(Long parentTagId) {
    TotaraTagDTO tag = totaraTagDAO.findParentTag(parentTagId);
    if (tag != null) {
      return new TagDTO(tag.getId(), tag.getName());
    } else {
      return null;
    }
  }
}
