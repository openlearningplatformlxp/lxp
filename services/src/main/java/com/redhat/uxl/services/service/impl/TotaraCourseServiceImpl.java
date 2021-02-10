package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraEventDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.datalayer.dto.EnrolledTotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseStatusDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFaceToFaceSessionDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramCourseType;
import com.redhat.uxl.dataobjects.domain.types.TotaraCourseSetCompletionType;
import com.redhat.uxl.services.service.MessageService;
import com.redhat.uxl.services.service.ProgramPlayerService;
import com.redhat.uxl.services.service.TotaraActivityService;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.services.service.dto.CourseDTO;
import com.redhat.uxl.services.service.dto.CourseUpcomingClassDTO;
import com.redhat.uxl.services.service.dto.LearningPathDTO;
import com.redhat.uxl.services.service.dto.ModuleDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseContentDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.services.service.dto.TotaraLeaningPathDTO;
import com.redhat.uxl.datalayer.dto.TotaraTextActivityDTO;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Totara course service.
 */
@Service
public class TotaraCourseServiceImpl implements TotaraCourseService {

  private static final Long REQUIRES_APPROVAL = new Long(4);

  /**
   * The Totara course dao.
   */
  @Inject
  TotaraCourseDAO totaraCourseDAO;
  /**
   * The Totara event dao.
   */
  @Inject
  TotaraEventDAO totaraEventDAO;
  /**
   * The Totara tag dao.
   */
  @Inject
  TotaraTagDAO totaraTagDAO;

  @Inject
  private TotaraActivityService activityService;

  /**
   * The Totara program service.
   */
  @Inject
  TotaraProgramService totaraProgramService;

  /**
   * The Program player service.
   */
  @Inject
  ProgramPlayerService programPlayerService;

  /**
   * The Messaging service.
   */
  @Inject
  MessageService messagingService;

  @Value("${totara.classroom.baseurl}")
  private String totaraClassroomLaunchUrl;

  private final Logger log = LoggerFactory.getLogger(TotaraCourseService.class);

  @Override
  public List<com.redhat.uxl.services.service.dto.TotaraCourseDTO> findActiveCourses() {
    log.info("Retrieving active courses");
    List<TotaraCourseDTO> courses = totaraCourseDAO.findActiveCourses();
    log.info("Found {} acitve courses", courses.size());
    List<com.redhat.uxl.services.service.dto.TotaraCourseDTO> retList = new ArrayList<>();
    for (TotaraCourseDTO course : courses) {
      retList.add(com.redhat.uxl.services.service.dto.TotaraCourseDTO.convertTotaraCourse(course));
    }
    log.info("Returning active courses");
    return retList;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TotaraCourseDTO> findActiveCourses(int page, int size) {

    return totaraCourseDAO.findActiveCourses(PageRequest.of(page, size));
  }

  @Override
  public List<TotaraLeaningPathDTO> findActivePrograms() {
    log.info("Retrieving active programs");
    List<TotaraProgramDTO> programs = totaraCourseDAO.findActivePrograms();
    log.info("Found {} acitve programs", programs.size());
    List<TotaraLeaningPathDTO> retList = new ArrayList<>();
    for (TotaraProgramDTO program : programs) {
      retList.add(TotaraLeaningPathDTO.convertToTotaraLearningPathForKeyValue(program));
    }
    log.info("Returning active programs");
    return retList;
  }

  @Override
  public List<CourseUpcomingClassDTO> findUpcomingClasses(Long courseId, Long personTotaraId) {
    List<TotaraFaceToFaceSessionDTO> faceToFaceSessions =
        totaraCourseDAO.findFaceToFaceSessions(courseId);
    List<CourseUpcomingClassDTO> dto = faceToFaceSessions.stream()
        .map(new Function<TotaraFaceToFaceSessionDTO, CourseUpcomingClassDTO>() {
          @Override
          public CourseUpcomingClassDTO apply(TotaraFaceToFaceSessionDTO totaraFaceToFaceSession) {
            CourseUpcomingClassDTO dto = new CourseUpcomingClassDTO();

            dto.setSessionId(totaraFaceToFaceSession.getSessionId());
            dto.setFaceToFaceId(totaraFaceToFaceSession.getFaceToFaceId());
            dto.setStartTime(totaraFaceToFaceSession.getStartDate());
            dto.setEndTime(totaraFaceToFaceSession.getEndDate());
            dto.setPrice(totaraFaceToFaceSession.getPrice());
            dto.setRequiresManagerApproval(
                REQUIRES_APPROVAL.equals(totaraFaceToFaceSession.getApprovalType()));
            dto.setSoldOut(false);
            dto.setEnrolled(false);
            dto.setRequestPending(messagingService
                .findCourseApprovalRequest(totaraFaceToFaceSession.getSessionId(), personTotaraId)
                .size() > 0);
            return dto;
          }
        }).collect(Collectors.toList());
    dto.sort((s1, s2) -> {
      if (s1.getEndTime().isBeforeNow()) {
        return 1;
      } else if (s2.getEndTime().isBeforeNow()) {
        return -1;
      } else {
        return s1.getStartTime().isBefore(s2.getStartTime()) ? -1 : 1;
      }
    });
    return dto;
  }

  @Override
  public LearningPathDTO findByProgramIdAndUserId(Long programId, Long userId) {
    List<TotaraProgramDTO> programList = totaraCourseDAO.findByUserIdAndProgramId(userId, programId);
    Map<Long, List<ModuleDTO>> courseModules = new HashMap<>();
    Map<Long, CourseDTO> programCourses = new HashMap<>();
    LearningPathDTO learningPath = new LearningPathDTO();
    boolean found = false;
    for (TotaraProgramDTO tp : programList) {
      if (!found) {
        found = true;
        learningPath.setProgramId(tp.getProgramId());
        learningPath.setProgramName(tp.getProgramName());
        learningPath.setProgramStatus(tp.getProgramStatus());
        learningPath.setProgramSummary(tp.getProgramSummary());
        learningPath.setProgramTrackingId(tp.getProgramTrackingId());
        learningPath.setCourses(new ArrayList<>());

        ModuleDTO mDto = new ModuleDTO();
        mDto.setModueleStatus(tp.getModuleStatus());
        mDto.setModuleId(tp.getModuleId());
        mDto.setModuleName(tp.getModuleName());
        mDto.setModuleTrackingId(tp.getModuleTrackingId());

        CourseDTO cDto = new CourseDTO();
        cDto.setCourseFullName(tp.getCourseFullName());
        cDto.setCourseId(tp.getCourseId());
        cDto.setCourseShortName(tp.getCourseShortName());
        cDto.setCourseStatus(tp.getCourseStatus());
        cDto.setCourseSummary(tp.getCourseSummary());
        cDto.setCourseTrackingId(tp.getCourseTrackingId());

        List<ModuleDTO> moduleDTOList = new ArrayList<>();
        moduleDTOList.add(mDto);

        courseModules.put(tp.getCourseId(), moduleDTOList);
        programCourses.put(tp.getCourseId(), cDto);
      } else {
        if (programCourses.containsKey(tp.getCourseId())) {
          ModuleDTO mDto = new ModuleDTO();
          mDto.setModueleStatus(tp.getModuleStatus());
          mDto.setModuleId(tp.getModuleId());
          mDto.setModuleName(tp.getModuleName());
          mDto.setModuleTrackingId(tp.getModuleTrackingId());
          if (courseModules.containsKey(tp.getCourseId())) {
            courseModules.get(tp.getCourseId()).add(mDto);
          } else {
            List<ModuleDTO> moduleDTOList = new ArrayList<>();
            moduleDTOList.add(mDto);

            courseModules.put(tp.getCourseId(), moduleDTOList);
          }

        } else {
          ModuleDTO mDto = new ModuleDTO();
          mDto.setModueleStatus(tp.getModuleStatus());
          mDto.setModuleId(tp.getModuleId());
          mDto.setModuleName(tp.getModuleName());
          mDto.setModuleTrackingId(tp.getModuleTrackingId());

          CourseDTO cDto = new CourseDTO();
          cDto.setCourseFullName(tp.getCourseFullName());
          cDto.setCourseId(tp.getCourseId());
          cDto.setCourseShortName(tp.getCourseShortName());
          cDto.setCourseStatus(tp.getCourseStatus());
          cDto.setCourseSummary(tp.getCourseSummary());
          cDto.setCourseTrackingId(tp.getCourseTrackingId());

          List<ModuleDTO> moduleDTOList = new ArrayList<>();
          moduleDTOList.add(mDto);

          courseModules.put(tp.getCourseId(), moduleDTOList);
          programCourses.put(tp.getCourseId(), cDto);

        }
      }
    }
    for (CourseDTO cDTO : programCourses.values()) {
      List<ModuleDTO> moduleDTOList = courseModules.get(cDTO.getCourseId());
      cDTO.setModules(new ArrayList<>());
      cDTO.getModules().addAll(moduleDTOList);
      learningPath.getCourses().add(cDTO);
    }

    return learningPath;
  }

  @Override
  public CoursePlayerCourseDTO getEnrolledCourse(Long personTotaraId, Long courseTotaraId) {
      return totaraCourseDAO.getEnrolledCourse(personTotaraId, courseTotaraId);
  }

    @Override
  public List<ProgramCourseSetDTO> getProgramCourseSets(Long personTotaraId, Long programId) {
    List<ProgramCourseSetDTO> courseSets = new ArrayList<>();
    log.debug("Looking for program course sets");
    com.redhat.uxl.datalayer.dto.TotaraProgramDTO programBO =
        programPlayerService.getProgramContentForUser(programId, personTotaraId);
    Map<Long, EnrolledTotaraCourseDTO> courseEnrollmentMap = programBO.getEnrolledCourses();

    List<TotaraCourseSetDTO> totaraCourseSets = programBO.getCourseSets();
    if (totaraCourseSets != null) {
      log.debug("Looking for program course sets -> Iterating");
      for (TotaraCourseSetDTO totaraCourseSet : totaraCourseSets) {
        ProgramCourseSetDTO courseSetDTO = new ProgramCourseSetDTO();
        courseSetDTO.setName(totaraCourseSet.getLabel());
        courseSetDTO.setSummary(totaraCourseSet.getDescription());
        courseSetDTO.setNextSetOperator(totaraCourseSet.getNextSetOperator());
        courseSetDTO.setDuration(totaraCourseSet.getDuration());

        List<TotaraCourseContentDTO> totaraCourses = totaraCourseSet.getCourses();
        List<ProgramCourseDTO> courses = new ArrayList<>();
        if (totaraCourses != null) {
          for (TotaraCourseContentDTO totaraCourse : totaraCourses) {
            ProgramCourseDTO courseDTO = new ProgramCourseDTO();
            courseDTO.setId(totaraCourse.getCourseID());
            courseDTO.setType(totaraCourse.getCourseType());
            courseDTO.setFullName(totaraCourse.getCourseFullName());
            courseDTO.setDescription(Jsoup.parse(totaraCourse.getDescription()).text());

            // check to see if these courses are nested programs or text entry
            log.debug(
                String.format("Looking for program course sets -> looking for nested program: %s",
                    totaraCourse.getCourseID()));
            Long courseProgramId =
                totaraProgramService.isCourseANestedProgram(totaraCourse.getCourseID());
            if (courseProgramId != null) {
              courseDTO.setType(ProgramCourseType.PROGRAM_LINK);
              courseDTO.setContentType("Program Link"); // TODO: (WJK) STEVE: These will need to be
                                                        // determined on a per-course basis (i.e.
                                                        // single
                                                        // activity content: video, URL, quiz, etc)
              courseDTO.setContentCaption("Program Link");
              courseDTO.setId(courseProgramId);

            } else {
              // Check to see if its a text entry
              log.debug(
                  String.format("Looking for program course sets -> looking for text entry: %s",
                      totaraCourse.getCourseID()));
              TotaraTextActivityDTO textId = totaraProgramService.isCourseATextEntry(personTotaraId,
                  totaraCourse.getCourseID());
              if (textId != null) {
                courseDTO.setType(ProgramCourseType.TEXT_ENTRY);
                courseDTO.setContentType("Text Entry"); // TODO: (WJK) STEVE: These will need to be
                                                        // determined on a per-course basis (i.e.
                                                        // single
                                                        // activity content: video, URL, quiz, etc)
                courseDTO.setContentCaption("Essay");
                courseDTO.setActivityId(textId.getInstance());
                courseDTO.setActivityMessage(textId.getIntro());
                courseDTO.setActivityValue(textId.getTextValue());

              } else {
                courseDTO.setContentType("Course"); // TODO: (WJK) STEVE: These will need to be
                                                    // determined on a per-course basis (i.e. single
                                                    // activity content: video, URL, quiz, etc)
                courseDTO.setContentCaption("Course"); // TODO: (WJK) STEVE: These will need to be
                                                       // determined on a per-course basis (i.e.
                                                       // single
                                                       // activity content, etc). Each type seems to
                                                       // have a different manner of displaying
                                                       // (i.e.
                                                       // duration for videos; "external exercise"
                                                       // for
                                                       // URLs; question count for quizes, etc)

              }
            }
            EnrolledTotaraCourseDTO courseEnrollment =
                courseEnrollmentMap.get(totaraCourse.getCourseID());

            ProgramCourseStatusDTO courseStatusDTO = new ProgramCourseStatusDTO();
            courseStatusDTO.setEnrolled(courseEnrollment != null);
            courseStatusDTO.setOptional(!totaraCourseSet.getCompletionType()
                .equals(TotaraCourseSetCompletionType.COMPLETIONTYPE_ALL));
            courseStatusDTO.setInProgress(
                courseEnrollment != null && courseEnrollment.getStatus().equals("IN_PROGRESS"));
            courseStatusDTO.setCompleted(
                courseEnrollment != null && courseEnrollment.getStatus().equals("COMPLETE"));
            courseStatusDTO.setLocked(totaraCourseSet.getIsLocked());

            courseDTO.setStatus(courseStatusDTO);

            courses.add(courseDTO);
          }
        }

        courseSetDTO.setCourses(courses);

        courseSets.add(courseSetDTO);
      }
    }

    return courseSets;
  }

  @Override
  @Transactional(readOnly = true)
  @Timed
  public List<CourseDocument> buildCourseDocuments(List<TotaraCourseDTO> courses) {
    return courses.parallelStream().map((totaraCourse) -> {
      CourseDocument courseDocument = new CourseDocument(totaraCourse);
      courseDocument
          .setVisibilityType(totaraCourseDAO.visibilityTypeForCourse(totaraCourse.getId()));
      if (CourseDocumentVisibilityType.RESTRICTED_AUDIENCE
          .equals(courseDocument.getVisibilityType())) {
        courseDocument.setAllowedUsers(
            totaraCourseDAO.findAllowedUserAudienceForCourse(totaraCourse.getId()));
      }
      courseDocument.setFirstTopicTag(totaraTagDAO.findCourseFirstTopic(totaraCourse.getId()));
      courseDocument.setSkillLevelTag(totaraTagDAO.findCourseSkillLevel(totaraCourse.getId()));
      courseDocument.setLanguageTag(totaraTagDAO.findCourseLanguage(totaraCourse.getId()));
      courseDocument.setTags(totaraTagDAO.findTagsForCourse(totaraCourse.getId()).stream()
          .map(TotaraTagDTO::getName).collect(Collectors.toList()));

      return courseDocument;
    }).collect(Collectors.toList());

  }

  @Override
  @Transactional(readOnly = true)
  @Timed
  public List<CourseDocument> buildCourseDocumentsFromProgram(List<TotaraProgramDTO> programs) {
    return programs.parallelStream().map((totaraProgram) -> {
      CourseDocument courseDocument = new CourseDocument(totaraProgram);
      courseDocument.setVisibilityType(
          totaraCourseDAO.visibilityTypeForLearningPath(totaraProgram.getProgramId()));
      if (CourseDocumentVisibilityType.PUBLIC
          .equals(courseDocument.getVisibilityType())) {
        courseDocument.setAllowedUsers(
            totaraCourseDAO.findAllowedUserAudienceForProgram(totaraProgram.getProgramId()));
      }
      courseDocument
          .setFirstTopicTag(totaraTagDAO.findLearningPathFirstTopic(totaraProgram.getProgramId()));
      courseDocument
          .setSkillLevelTag(totaraTagDAO.findLearningPathSkillLevel(totaraProgram.getProgramId()));
      courseDocument
          .setLanguageTag(totaraTagDAO.findLearningPathLanguage(totaraProgram.getProgramId()));
      courseDocument.setTags(totaraTagDAO.findTagsForProgram(totaraProgram.getProgramId()).stream()
          .map(TotaraTagDTO::getName).collect(Collectors.toList()));
      return courseDocument;
    }).collect(Collectors.toList());
  }

  @Override
  public List<CourseDocument> buildCourseDocumentsFromEvent(List<TotaraEventDTO> events) {
    return events.parallelStream().map((totaraEvent) -> {
      CourseDocument courseDocument =
          new CourseDocument(totaraEvent, totaraClassroomLaunchUrl + totaraEvent.getSessionId());
      return courseDocument;
    }).collect(Collectors.toList());
  }

  @Override
  public Page<TotaraProgramDTO> findActivePrograms(int page, int size) {
    return totaraCourseDAO.findActivePrograms(PageRequest.of(page, size));
  }

  @Override
  public Page<TotaraEventDTO> findActiveEvents(int page, int size) {
    return totaraEventDAO.findActiveEvents(PageRequest.of(page, size));
  }

  @Override
  public void updateTextEntryResult(Long userId, Long activityId, Long courseId, String message) {
    Validate.notNull(userId);
    Validate.notNull(activityId);
    Validate.notNull(courseId);

    totaraCourseDAO.inertActivityText(activityId, userId, message);

    // Complete the activity and course
    Long cmID = totaraCourseDAO.findActivityId(activityId, courseId);
    if (cmID == null) {

    } else {
      // Complete activity
      totaraCourseDAO.completeTotaraCourseActivityDB(cmID, userId);
      activityService.completeURLSingleActCourse(courseId, userId);

    }
    // Complete course

  }
}
