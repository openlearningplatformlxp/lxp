package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraEventDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.services.service.CourseService;
import com.redhat.uxl.services.service.PersonAccessService;
import com.redhat.uxl.services.service.TotaraActivityService;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import com.redhat.uxl.services.service.TotaraSectionService;
import com.redhat.uxl.services.service.dto.CourseActivityStatusDTO;
import com.redhat.uxl.services.service.dto.CourseDTO;
import com.redhat.uxl.services.service.dto.CourseOverviewDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import com.redhat.uxl.services.service.dto.CourseProgressDTO;
import com.redhat.uxl.services.service.dto.CourseProgressEventDTO;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseSectionsDTO;
import com.redhat.uxl.services.service.dto.CourseTagDTO;
import com.redhat.uxl.services.service.dto.LocationDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * The type Course service.
 */
@Service
public class CourseServiceImpl implements CourseService {

    /**
     * The Totara tag dao.
     */
    @Inject
  TotaraTagDAO totaraTagDAO;

    /**
     * The Totara event dao.
     */
    @Inject
  TotaraEventDAO totaraEventDAO;

    /**
     * The Totara course dao.
     */
    @Inject
  TotaraCourseDAO totaraCourseDAO;

    /**
     * The Totara section service.
     */
    @Inject
  TotaraSectionService totaraSectionService;

    /**
     * The Totara activity service.
     */
    @Inject
  TotaraActivityService totaraActivityService;

    /**
     * The Totara enrollment service.
     */
    @Inject
  TotaraEnrollmentService totaraEnrollmentService;

    /**
     * The Person access service.
     */
    @Inject
  PersonAccessService personAccessService;

  private final Logger log = LoggerFactory.getLogger(CourseService.class);

  @Override
  public CourseDTO getCourse(Long personTotaraId, Long courseId) {
    TotaraCourseDTO course = totaraCourseDAO.findCourseByCourseId(courseId);

    CourseDTO cDto = new CourseDTO();
    cDto.setCourseId(course.getId());
    cDto.setCourseFullName(course.getFullName());
    cDto.setCourseShortName(course.getShortName());
    cDto.setEnrolled(totaraEnrollmentService.isUserEnrolledInCourse(courseId, personTotaraId));
    TotaraTagDTO firstTopic = totaraTagDAO.findCourseFirstTopic(courseId);
    if (firstTopic != null) {
      cDto.setFirstTopic(firstTopic.getName());
    }

    return cDto;
  }

  @Override
  public CourseOverviewDTO getCourseOverview(Long personTotaraId, Long courseId) {
    TotaraCourseDTO course = totaraCourseDAO.findCourseByCourseId(courseId);

    personAccessService.registerAccessToCourse(personTotaraId, courseId);

    CourseOverviewDTO courseOverviewDTO = new CourseOverviewDTO();

    courseOverviewDTO.setDescription(course.getSummary());

    // Lets do Tags
    List<TotaraTagDTO> tags = totaraTagDAO.findTagsForCourseWithParent(courseId);
    if (tags != null) {

      courseOverviewDTO.setTagsTypes(new ArrayList<>());
      // Create array
      String tagname = "";
      CourseTagDTO cdt = null;
      for (TotaraTagDTO tag : tags) {
        if (tag.getName().equalsIgnoreCase(tagname)) {
          cdt.getTags().add(tag.getRawname());
        } else {
          if (cdt != null)
            courseOverviewDTO.getTagsTypes().add(cdt);
          tagname = tag.getName();
          cdt = new CourseTagDTO();
          cdt.setName(tag.getName());
          cdt.getTags().add(tag.getRawname());

        }
      }
      // Add final
      if (cdt != null)
        courseOverviewDTO.getTagsTypes().add(cdt);
    }
    return courseOverviewDTO;
  }

  @Timed
  @Override
  @Transactional(readOnly = true)
  public CourseSectionsDTO getPrerequisitesCourseSections(Long courseId) {
    List<CoursePlayerSectionDTO> sections =
        totaraSectionService.getPrerequisitesSectionsHaveActivitiesForCourse(courseId);
    return getCourseSections(sections, courseId);
  }

  @Timed
  @Override
  @Transactional(readOnly = true)
  public CourseSectionsDTO getResourcesCourseSections(Long courseId) {
    List<CoursePlayerSectionDTO> sections =
        totaraSectionService.getResourceSectionsHaveActivitiesForCourse(courseId);
    return getCourseSections(sections, courseId);
  }

  @Timed
  @Override
  @Transactional(readOnly = true)
  public CourseSectionsDTO getGeneralCourseSections(Long courseId, Long personTotaraId) {
    List<CoursePlayerSectionDTO> sections =
        totaraSectionService.getGeneralSectionsHaveActivitiesForCourse(courseId);

    CourseSectionsDTO dto = getCourseSections(sections, courseId);
    dto.setActivityStatuses(CourseActivityStatusDTO.valueOfAvailable(
        totaraActivityService.getStatusForActivitiesInCourse(courseId, personTotaraId)));
    dto.setCourseProgress(determineCourseProgress(dto));

    return dto;
  }

  private CourseSectionsDTO getCourseSections(List<CoursePlayerSectionDTO> sections,
      Long courseId) {

    CourseSectionsDTO courseSectionsDTO = new CourseSectionsDTO();
    courseSectionsDTO.setSections(sections);

    // TODO: (WJK) Replace this with something more efficient
    for (CoursePlayerSectionDTO section : sections) {
      List<CoursePlayerActivityDTO> activities =
          totaraActivityService.getActivitiesBySection(courseId, section.getId());

      section.setActivities(activities);
      section.sortActivities();
    }

    return courseSectionsDTO;
  }

  private CourseProgressDTO determineCourseProgress(CourseSectionsDTO sections) {
    int activityCount = 0;
    int activityCompleteCount = 0;

    for (CoursePlayerSectionDTO section : sections.getSections()) {
      for (CoursePlayerActivityDTO activity : section.getActivities()) {
        activityCount++;

        CourseActivityStatusDTO activityStatus =
            sections.getActivityStatuses().get(activity.getId());
        if (activityStatus != null && activityStatus.getStatus() == 1) {
          activityCompleteCount++;
        }
      }
    }

    CourseProgressDTO progressDTO = new CourseProgressDTO();
    progressDTO.setActivityCount(activityCount);
    progressDTO.setActivityCompleteCount(activityCompleteCount);

    if (activityCount == 0) {
      progressDTO.setPercentComplete(100);
    } else {
      progressDTO.setPercentComplete(
          (int) Math.ceil(((float) activityCompleteCount / (float) activityCount) * 100));
    }

    return progressDTO;
  }

  @Override
  public CourseProgressionOverviewDTO getCourseProgressionOverview(Long courseId,
      Long personTotaraId) {

    // Get course, get sections, get activities
    CourseSectionsDTO courseSections = getGeneralCourseSections(courseId, personTotaraId);

    // TODO: (WJK) Replace with actual implementation

    CourseProgressionOverviewDTO progressDTO = new CourseProgressionOverviewDTO();
    progressDTO.setActivityCount(totaraActivityService.getActivityCount(courseId));
    progressDTO.setActivityCompleteCount(
        totaraActivityService.getCompletedActivityCount(courseId, personTotaraId));


    int percentComplete = (int) Math.ceil(
        ((float) progressDTO.getActivityCompleteCount() / (float) progressDTO.getActivityCount())
            * 100);
    progressDTO.setPercentComplete(percentComplete);

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    progressDTO.setProgressEvents(new ArrayList<>());

    List<CoursePlayerSectionDTO> sections = courseSections.getSections();

    for (CoursePlayerSectionDTO section : sections) {
      List<CoursePlayerActivityDTO> activities = section.getActivities();
      for (CoursePlayerActivityDTO activity : activities) {

        CourseActivityStatusDTO activityStatus =
            courseSections.getActivityStatuses().get(activity.getId());

        String status = "NOT_STARTED";
        DateTime time = null;

        if (activityStatus != null) {
          if (activityStatus.getStatus() == 0) {
            status = "NOT_STARTED";
          } else if (activityStatus.getStatus() == 1) {
            status = "COMPLETE";
            if (activityStatus.getTimecompleted() != null) {
              time = new DateTime((long) activityStatus.getTimecompleted() * 1000);

            }
          } else if (activityStatus.getStatus() == 2) {
            status = "NOT_STARTED";
          }

        }
        progressDTO.getProgressEvents().add(new CourseProgressEventDTO(status,
            activity.getType().name(), "Complete the " + activity.getName(), time));

      }
    }
    return progressDTO;
  }

  @Override
  public List<LocationDTO> getEventLocations() {
    Map<String, List<TotaraEventDTO>> countryMap =
        totaraEventDAO.findEventLocations().stream().filter(e -> e.getCountry() != null)
            .collect(Collectors.groupingBy(TotaraEventDTO::getCountry));
    return countryMap.entrySet().stream()
        .map((c) -> new LocationDTO(c.getKey(),
            c.getValue().stream().map(event -> event.getCity()).sorted().collect(toList())))
        .sorted().collect(toList());

  }
}
