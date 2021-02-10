package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.dao.TotaraSectionDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import com.redhat.uxl.datalayer.sql.totara.TotaraSectionSQL;
import com.redhat.uxl.services.service.TotaraFileService;
import com.redhat.uxl.services.service.TotaraSectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Totara section service.
 */
@Service
@Slf4j
public class TotaraSectionServiceImpl implements TotaraSectionService {


  @Inject
  private TotaraFileService totaraFileService;

  @Inject
  private TotaraSectionDAO totaraSectionDAO;

  @Override
  public List<CoursePlayerSectionDTO> getResourceSectionsHaveActivitiesForCourse(Long courseId) {
    String query = TotaraSectionSQL.getCourseResourcesHaveActivitiesSQL.replace(":courseId",
        courseId.toString());
    return getSectionsHaveActivitiesForCourse(query, courseId);
  }

  @Override
  public List<CoursePlayerSectionDTO> getPrerequisitesSectionsHaveActivitiesForCourse(
      Long courseId) {
    String query = TotaraSectionSQL.getCoursePrerequisitesHaveActivitiesSQL.replace(":courseId",
        courseId.toString());
    return getSectionsHaveActivitiesForCourse(query, courseId);
  }

  @Override
  public List<CoursePlayerSectionDTO> getGeneralSectionsHaveActivitiesForCourse(Long courseId) {
    String query = TotaraSectionSQL.getCourseSectionsHaveActivitiesSQL.replace(":courseId",
        courseId.toString());
    return getSectionsHaveActivitiesForCourse(query, courseId);
  }

  private List<CoursePlayerSectionDTO> getSectionsHaveActivitiesForCourse(String query,
      Long courseId) {
    log.debug(query);
    List<Map<String, Object>> sections = totaraSectionDAO.getSectionsHaveActivitiesForCourse(query);
    List<CoursePlayerSectionDTO> sectionsList = new ArrayList<>();

    // if a section is not giving a title default it to "Topic X"
    int index = 0;
    CoursePlayerSectionDTO bo;
    for (Map<String, Object> row : sections) {
      bo = new CoursePlayerSectionDTO(row);
      bo.setSummary(totaraFileService.buildPluginUrls(bo.getId(), bo.getSummary()));
      if (StrUtils.isEmpty(bo.getName())) {
        bo.setName("Topic " + index);
      }
      sectionsList.add(bo);
      index++;
    }
    return sectionsList;
  }

  @Override
  public CoursePlayerSectionDTO getSectionForCourse(Long courseId, Long sectionId) {
    return totaraSectionDAO.getSectionForCourse(courseId, sectionId);
  }
}
