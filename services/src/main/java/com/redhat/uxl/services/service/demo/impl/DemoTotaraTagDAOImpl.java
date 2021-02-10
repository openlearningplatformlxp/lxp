package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.services.service.demo.dto.CourseTagsDTO;
import com.redhat.uxl.services.service.demo.dto.ParentTagDTO;
import com.redhat.uxl.services.service.demo.dto.ProgramTagDTO;
import com.redhat.uxl.services.service.demo.dto.UserTagDTO;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Demo totara tag dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraTagDAOImpl implements TotaraTagDAO {

  /**
   * The Demo utility service.
   */
  @Inject DemoUtilityService demoUtilityService;

  @Override
  public List<TotaraTagDTO> findUnmatchedWikiRoleTags(Long wikiId, String searchTerm, int max) {
    return new ArrayList<TotaraTagDTO>();
  }

  @Override
  public List<TotaraTagDTO> findWikiTags(Long id) {
    return null;
  }

  @Override
  public List<TotaraTagDTO> findRoleTags() {
    List<UserTagDTO> dto = demoUtilityService.getRoleLevelTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();

    List<TotaraTagDTO> result = new ArrayList<>();

    for (UserTagDTO ut : dto) {
      for (TotaraTagDTO tag : all) {
        if (ut.getTagId().equals(tag.getId())) {
          result.add(tag);
        }
      }
    }
    return result;
  }

  @Override
  public List<TotaraTagDTO> findUnmatchedUserTags(Long profileId, String searchTerm, int max) {
    return null;
  }

  @Override
  public List<TotaraTagDTO> findAllUserTags(Long profileId) {
    return null;
  }

  @Override
  public List<TotaraTagDTO> findTopicTags() {
    List<UserTagDTO> dto = demoUtilityService.getTopicLevelTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();

    List<TotaraTagDTO> result = new ArrayList<>();

    for (UserTagDTO ut : dto) {
      for (TotaraTagDTO tag : all) {
        if (ut.getTagId().equals(tag.getId())) {
          result.add(tag);
        }
      }
    }

    return result;
  }

  @Override
  public List<TotaraTagDTO> findSkillLevelTags() {
    List<ProgramTagDTO> dto = demoUtilityService.getSkillLevelTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();

    List<TotaraTagDTO> result = new ArrayList<>();

    for (ProgramTagDTO ut : dto) {
      for (TotaraTagDTO tag : all) {
        if (ut.getTagId().equals(tag.getId())) {
          result.add(tag);
        }
      }
    }
    return result;
  }

  @Override
  public List<TotaraTagDTO> findLanguageTags() {
    List<ProgramTagDTO> dto = demoUtilityService.getLanguageLevelTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();

    List<TotaraTagDTO> result = new ArrayList<>();

    for (ProgramTagDTO ut : dto) {
      for (TotaraTagDTO tag : all) {
        if (ut.getTagId().equals(tag.getId())) {
          result.add(tag);
        }
      }
    }
    return result;
  }

  @Override
  public List<TotaraTagDTO> findTagsForCourse(Long courseId) {
    List<CourseTagsDTO> dto = demoUtilityService.getCourseTags();
    List<CourseTagsDTO> filtered = new ArrayList<>();
    List<TotaraTagDTO> all = demoUtilityService.getTags();
    List<TotaraTagDTO> result = new ArrayList<>();

    for (CourseTagsDTO a : dto) {
      if (a.getCourseId().equals(courseId)) {
        filtered.add(a);
      }
    }

    for (CourseTagsDTO a : filtered) {
      for (TotaraTagDTO tag : all) {
        if (tag.getId().equals(a.getTagId())) {
          result.add(tag);
        }
      }
    }

    return result;
  }

  @Override
  public List<TotaraTagDTO> findTagsForProgram(Long programId) {
    List<ProgramTagDTO> programTags = demoUtilityService.getProgramTags();
    List<TotaraTagDTO> tags = demoUtilityService.getTags();
    List<TotaraTagDTO> list = new ArrayList<>();

    for (ProgramTagDTO programTag : programTags) {
      for (TotaraTagDTO tag : tags) {
        if (programTag.getTagId() == tag.getId()) {
          list.add(tag);
        }
      }
    }
    return list;
  }

  @Override
  public List<TotaraTagDTO> findTagsForProgramWithParent(Long programId) {
    return null;
  }

  @Override
  public TotaraTagDTO findCourseSkillLevel(Long courseId) {
    List<CourseTagsDTO> skillLevel = demoUtilityService.getCourseSkillTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();
    CourseTagsDTO select = null;
    for (CourseTagsDTO a : skillLevel) {
      if (a.getCourseId().equals(courseId)) {
        select = a;
      }
    }

    if (select != null) {
      for (TotaraTagDTO tag : all) {
        if (tag.getId().equals(select.getTagId())) {
          return tag;
        }
      }
    }

    return null;
  }

  @Override
  public TotaraTagDTO findLearningPathSkillLevel(Long programId) {
    List<ProgramTagDTO> programTags = demoUtilityService.getSkillLevelTags();
    List<TotaraTagDTO> tags = demoUtilityService.getTags();
    Long tagId = -1L;

    for (ProgramTagDTO tag : programTags) {
      if (tag.getProgramId() == programId) {
        tagId = tag.getTagId();
      }
    }
    if (tagId != -1L) {
      for (TotaraTagDTO tag : tags) {
        if (tag.getId() == tagId) {
          return tag;
        }
      }
    }

    return new TotaraTagDTO();
  }

  @Override
  public List<TotaraTagDTO> findTagsForCourseWithParent(Long programId) {
    return null;
  }

  @Override
  public TotaraTagDTO findCourseLanguage(Long courseId) {
    List<CourseTagsDTO> language = demoUtilityService.getCourseLanguageTags();
    List<TotaraTagDTO> all = demoUtilityService.getTags();
    CourseTagsDTO select = null;
    for (CourseTagsDTO a : language) {
      if (a.getCourseId().equals(courseId)) {
        select = a;
      }
    }

    if (select != null) {
      for (TotaraTagDTO tag : all) {
        if (tag.getId().equals(select.getTagId())) {
          return tag;
        }
      }
    }

    return null;
  }

  @Override
  public TotaraTagDTO findLearningPathLanguage(Long programId) {
    List<TotaraTagDTO> allTags = demoUtilityService.getTags();
    List<ProgramTagDTO> programTags = demoUtilityService.getLanguageLevelTags();

    Long tagId = -1L;

    for (ProgramTagDTO pTag : programTags) {
      if (pTag.getProgramId() == programId) {
        tagId = pTag.getTagId();
      }
    }

    for (TotaraTagDTO tag : allTags) {
      if (tag.getId() == tagId) {
        return tag;
      }
    }
    return new TotaraTagDTO();
  }

  @Override
  public TotaraTagDTO findCourseFirstTopic(Long courseId) {
    List<CourseTagsDTO> all = demoUtilityService.getCourseTags();
    List<TotaraTagDTO> allTags = demoUtilityService.getTags();
    CourseTagsDTO select = null;
    for (CourseTagsDTO a : all) {
      if (a.getCourseId().equals(courseId)) {
        select = a;
      }
    }

    if (select != null) {
      for (TotaraTagDTO t : allTags) {
        if (t.getId().equals(select.getTagId())) {
          return t;
        }
      }
    }
    return null;
  }

  @Override
  public List<TotaraTagDTO> findCourseFirstTopic(List<Long> courseId) {
    List<CourseTagsDTO> all = demoUtilityService.getCourseTags();
    List<TotaraTagDTO> allTags = demoUtilityService.getTags();
    List<CourseTagsDTO> select = new ArrayList<>();
    List<TotaraTagDTO> result = new ArrayList<>();

    for (CourseTagsDTO a : all) {
      for (Long id : courseId) {
        if (a.getCourseId().equals(id)) {
          select.add(a);
        }
      }
    }

    for (TotaraTagDTO t : allTags) {
      for (CourseTagsDTO c : select) {
        if (t.getId().equals((c.getTagId()))) {
          result.add(t);
        }
      }

    }

    return result;
  }

  @Override
  public TotaraTagDTO findLearningPathFirstTopic(Long programId) {
    List<CourseTagsDTO> topicTags = demoUtilityService.getCourseTopicTags();
    List<TotaraTagDTO> tags = demoUtilityService.getTags();
    List<ProgramTagDTO> programTags = demoUtilityService.getProgramTags();
    Long tagId = -1L;
    for (ProgramTagDTO programTag : programTags) {
      if (programTag.getProgramId() == programId) {
        tagId = programTag.getTagId();
      }
    }

    if (tagId != -1L) {
      for (TotaraTagDTO tag : tags) {
        if (tag.getId() == tagId) return tag;
      }
    }

    return new TotaraTagDTO();
  }

  @Override
  public TotaraTagDTO findTag(Long tagId) {
    return null;
  }

  @Override
  public List<TotaraTagDTO> findParentTags() {
    List<ParentTagDTO> parentTags = demoUtilityService.getParentTags();
    List<TotaraTagDTO> tags = demoUtilityService.getTags();
    List<TotaraTagDTO> retTags = new ArrayList<>();
    for (ParentTagDTO dto : parentTags) {
      for (TotaraTagDTO t : tags) {
        if (dto.getId().equals(t.getId())) {
          retTags.add(t);
        }
      }
    }
    return retTags;
  }

  @Override
  public List<TotaraTagDTO> findChildTags(Long parentTagId) {
    List<ParentTagDTO> parentTags = demoUtilityService.getParentTags();
    List<TotaraTagDTO> tags = demoUtilityService.getTags();
    List<TotaraTagDTO> retTags = new ArrayList<>();
    for (ParentTagDTO dto : parentTags) {
      if (dto.getId().equals(parentTagId)) {
        for (Long childId : dto.getSubtags()) {
          for (TotaraTagDTO t : tags) {
            if (childId.equals(t.getId())) {
              retTags.add(t);
            }
          }
        }
      }
    }
    return retTags;
  }

  @Override
  public TotaraTagDTO findParentTag(Long tagId) {
    return null;
  }
}
