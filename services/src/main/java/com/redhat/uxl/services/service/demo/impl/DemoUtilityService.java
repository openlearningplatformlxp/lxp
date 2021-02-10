package com.redhat.uxl.services.service.demo.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraAudienceDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFeedbackDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraMessageDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.demo.dto.ActiveEventDTO;
import com.redhat.uxl.services.service.demo.dto.ActiveProgramDTO;
import com.redhat.uxl.services.service.demo.dto.ActivityInstanceDTO;
import com.redhat.uxl.services.service.demo.dto.CeCreditsUserDTO;
import com.redhat.uxl.services.service.demo.dto.CourseAudienceDTO;
import com.redhat.uxl.services.service.demo.dto.CourseListDTO;
import com.redhat.uxl.services.service.demo.dto.CourseTagsDTO;
import com.redhat.uxl.services.service.demo.dto.ParentTagDTO;
import com.redhat.uxl.services.service.demo.dto.ProgramCourseDTO;
import com.redhat.uxl.services.service.demo.dto.ProgramTagDTO;
import com.redhat.uxl.services.service.demo.dto.UserActivityDTO;
import com.redhat.uxl.services.service.demo.dto.UserAudienceDTO;
import com.redhat.uxl.services.service.demo.dto.UserCourseDTO;
import com.redhat.uxl.services.service.demo.dto.UserEventDTO;
import com.redhat.uxl.services.service.demo.dto.UserInterestsDTO;
import com.redhat.uxl.services.service.demo.dto.UserManagerDTO;
import com.redhat.uxl.services.service.demo.dto.UserProgramDTO;
import com.redhat.uxl.services.service.demo.dto.UserTagDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * The type Demo utility service.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:false}")
public class DemoUtilityService {
  /**
   * The File location.
   */
  @Value("${demo.file.location}")
  String fileLocation;

  /**
   * The Users file.
   */
  @Value("${demo.users}")
  String usersFile;

  /**
   * The Managers file.
   */
  @Value("${demo.managers}")
  String managersFile;

  /**
   * The Audience file.
   */
  @Value("${demo.audiences}")
  String audienceFile;

  /**
   * The User audience file.
   */
  @Value("${demo.userAudiences}")
  String userAudienceFile;

  /**
   * The Tag file.
   */
  @Value("${demo.tags}")
  String tagFile;

  /**
   * The User interests file.
   */
  @Value("${demo.userInterests}")
  String userInterestsFile;

  /**
   * The Courses file.
   */
  @Value("${demo.courses}")
  String coursesFile;

  /**
   * The Programs file.
   */
  @Value("${demo.programs}")
  String programsFile;

  /**
   * The In progress programs file.
   */
  @Value("${demo.inProgressPrograms}")
  String inProgressProgramsFile;

  /**
   * The Roles file.
   */
  @Value("${demo.roles}")
  String rolesFile;

  /**
   * The User roles file.
   */
  @Value("${demo.userRoles}")
  String userRolesFile;

  /**
   * The User feedback file.
   */
  @Value("${demo.userFeedback}")
  String userFeedbackFile;

  /**
   * The Program tags file.
   */
  @Value("${demo.programTags}")
  String programTagsFile;

  /**
   * The Course tags file.
   */
  @Value("${demo.courseTags}")
  String courseTagsFile;

  /**
   * The Events file.
   */
  @Value("${demo.events}")
  String eventsFile;

  /**
   * The User programs file.
   */
  @Value("${demo.userPrograms}")
  String userProgramsFile;

  /**
   * The Messages file.
   */
  @Value("${demo.messages}")
  String messagesFile;

  /**
   * The Active courses file.
   */
  @Value("${demo.activeCourses}")
  String activeCoursesFile;

  /**
   * The Active programs file.
   */
  @Value("${demo.activePrograms}")
  String activeProgramsFile;

  /**
   * The Program courses file.
   */
  @Value("${demo.programCourses}")
  String programCoursesFile;

  /**
   * The Active course audiences file.
   */
  @Value("${demo.activeCourseAudiences}")
  String activeCourseAudiencesFile;

  /**
   * The Programs completed file.
   */
  @Value("${demo.programsCompleted}")
  String programsCompletedFile;

  /**
   * The User courses file.
   */
  @Value("${demo.userCourses}")
  String userCoursesFile;

  /**
   * The User events file.
   */
  @Value("${demo.userEvents}")
  String userEventsFile;

  /**
   * The Active events file.
   */
  @Value("${demo.activeEvents}")
  String activeEventsFile;

  /**
   * The Ce credits file.
   */
  @Value("${demo.ceCredits}")
  String ceCreditsFile;

  /**
   * The Courses in progress file.
   */
  @Value("${demo.coursesInProgress}")
  String coursesInProgressFile;

  /**
   * The Activity file.
   */
  @Value("${demo.activity}")
  String activityFile;

  /**
   * The Activity instance file.
   */
  @Value("${demo.activityInstance}")
  String activityInstanceFile;

  /**
   * The User activities file.
   */
  @Value("${demo.userActivities}")
  String userActivitiesFile;

  /**
   * The Tags skill level file.
   */
  @Value("${demo.tagsSkillLevel}")
  String tagsSkillLevelFile;

  /**
   * The Tags language level file.
   */
  @Value("${demo.tagsLanguageLevel}")
  String tagsLanguageLevelFile;

  /**
   * The Tags role level file.
   */
  @Value("${demo.tagsRoleLevel}")
  String tagsRoleLevelFile;

  /**
   * The Tags topic level file.
   */
  @Value("${demo.tagsTopicLevel}")
  String tagsTopicLevelFile;

  /**
   * The Course tags topic file.
   */
  @Value("${demo.courseTagsTopic}")
  String courseTagsTopicFile;

  /**
   * The Course tags skill file.
   */
  @Value("${demo.courseTagsSkill}")
  String courseTagsSkillFile;

  /**
   * The Course tags language file.
   */
  @Value("${demo.courseTagsLanguage}")
  String courseTagsLanguageFile;

  /**
   * The Parent tags file.
   */
  @Value("${demo.parentTags}")
  String parentTagsFile;

  /**
   * The constant messageMap.
   */
  public static Map<Long, TotaraMessageDTO> messageMap = new HashMap<>();

  /**
   * Init.
   */
  @PostConstruct
  public void init() {
    log.debug("Loading Message Map");
    messageMap = getAllMessages().stream()
        .collect(Collectors.toMap(TotaraMessageDTO::getId, totaraMessage -> totaraMessage));
  }

  private <T> Object parseResponse(String fileName, Class<T> clazz, Boolean failUnknown) {
    ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failUnknown);
    try {
      return mapper.readValue(new ClassPathResource(fileName).getFile(), clazz);
    } catch (Exception e) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Error occurred trying to parse " + clazz.getName());
    }
  }

  /**
   * Gets users.
   *
   * @return the users
   */
  List<TotaraUserDTO> getUsers() {
    TotaraUserDTO[] users =
        (TotaraUserDTO[]) parseResponse(fileLocation + usersFile, TotaraUserDTO[].class, false);
    return Arrays.asList(users);
  }

  /**
   * Gets managers.
   *
   * @return the managers
   */
  List<UserManagerDTO> getManagers() {
    UserManagerDTO[] managers = (UserManagerDTO[]) parseResponse(fileLocation + managersFile,
        UserManagerDTO[].class, false);
    return Arrays.asList(managers);
  }

  /**
   * Gets audiences.
   *
   * @return the audiences
   */
  List<TotaraAudienceDTO> getAudiences() {
    TotaraAudienceDTO[] audiences = (TotaraAudienceDTO[]) parseResponse(fileLocation + audienceFile,
        TotaraAudienceDTO[].class, false);
    return Arrays.asList(audiences);
  }

  /**
   * Gets user audiences.
   *
   * @return the user audiences
   */
  List<UserAudienceDTO> getUserAudiences() {
    UserAudienceDTO[] userAudiences =
        (UserAudienceDTO[]) parseResponse(fileLocation + userAudienceFile, UserAudienceDTO[].class,
            false);
    return Arrays.asList(userAudiences);
  }

  /**
   * Gets tags.
   *
   * @return the tags
   */
  List<TotaraTagDTO> getTags() {
    TotaraTagDTO[] userAudiences =
        (TotaraTagDTO[]) parseResponse(fileLocation + tagFile, TotaraTagDTO[].class, false);
    return Arrays.asList(userAudiences);
  }

  /**
   * Gets user interests.
   *
   * @return the user interests
   */
  List<UserInterestsDTO> getUserInterests() {
    UserInterestsDTO[] userAudiences =
        (UserInterestsDTO[]) parseResponse(fileLocation + userInterestsFile,
            UserInterestsDTO[].class, false);
    return Arrays.asList(userAudiences);
  }

  /**
   * Gets courses.
   *
   * @return the courses
   */
  List<TotaraCourseDTO> getCourses() {
    TotaraCourseDTO[] courses =
        (TotaraCourseDTO[]) parseResponse(fileLocation + coursesFile, TotaraCourseDTO[].class, false);
    return Arrays.asList(courses);
  }

  /**
   * Gets active courses.
   *
   * @return the active courses
   */
  List<CourseListDTO> getActiveCourses() {
    CourseListDTO[] courses = (CourseListDTO[]) parseResponse(fileLocation + activeCoursesFile,
        CourseListDTO[].class, false);
    return Arrays.asList(courses);
  }

  /**
   * Gets programs.
   *
   * @return the programs
   */
  List<TotaraProgramDTO> getPrograms() {
    TotaraProgramDTO[] programs =
        (TotaraProgramDTO[]) parseResponse(fileLocation + programsFile, TotaraProgramDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets active programs.
   *
   * @return the active programs
   */
  List<ActiveProgramDTO> getActivePrograms() {
    ActiveProgramDTO[] programs =
        (ActiveProgramDTO[]) parseResponse(fileLocation + activeProgramsFile,
            ActiveProgramDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets user programs.
   *
   * @return the user programs
   */
  List<UserProgramDTO> getUserPrograms() {
    UserProgramDTO[] programs =
        (UserProgramDTO[]) parseResponse(fileLocation + userProgramsFile, UserProgramDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets roles.
   *
   * @return the roles
   */
  List<TotaraTagDTO> getRoles() {
    TotaraTagDTO[] userAudiences =
        (TotaraTagDTO[]) parseResponse(fileLocation + rolesFile, TotaraTagDTO[].class, false);
    return Arrays.asList(userAudiences);
  }

  /**
   * Gets user roles.
   *
   * @return the user roles
   */
  List<UserInterestsDTO> getUserRoles() {
    UserInterestsDTO[] userAudiences =
        (UserInterestsDTO[]) parseResponse(fileLocation + userRolesFile, UserInterestsDTO[].class,
            false);
    return Arrays.asList(userAudiences);

  }

  /**
   * Gets program tags.
   *
   * @return the program tags
   */
  List<ProgramTagDTO> getProgramTags() {
    ProgramTagDTO[] tagPairs = (ProgramTagDTO[]) parseResponse(fileLocation + programTagsFile,
        ProgramTagDTO[].class, false);
    return Arrays.asList(tagPairs);
  }

  /**
   * Gets course tags.
   *
   * @return the course tags
   */
  List<CourseTagsDTO> getCourseTags() {

    List<CourseTagsDTO> tags = new ArrayList<>();

    tags = Stream.concat(getCourseSkillTags().stream(), getCourseTopicTags().stream())
        .collect(toList());
    return Stream.concat(tags.stream(), getCourseLanguageTags().stream()).collect(toList());
  }

  /**
   * Gets user feedback.
   *
   * @return the user feedback
   */
  List<TotaraFeedbackDTO> getUserFeedback() {
    TotaraFeedbackDTO[] userFeedback =
        (TotaraFeedbackDTO[]) parseResponse(fileLocation + userFeedbackFile, TotaraFeedbackDTO[].class,
            false);
    return Arrays.asList(userFeedback);

  }

  /**
   * Gets all events.
   *
   * @return the all events
   */
  List<TotaraEventDTO> getAllEvents() {
    TotaraEventDTO[] events =
        (TotaraEventDTO[]) parseResponse(fileLocation + eventsFile, TotaraEventDTO[].class, false);
    return Arrays.asList(events);
  }

  /**
   * Gets all messages.
   *
   * @return the all messages
   */
  List<TotaraMessageDTO> getAllMessages() {
    TotaraMessageDTO[] messages =
        (TotaraMessageDTO[]) parseResponse(fileLocation + messagesFile, TotaraMessageDTO[].class, false);
    return Arrays.asList(messages);
  }

  /**
   * Gets program courses.
   *
   * @return the program courses
   */
  List<ProgramCourseDTO> getProgramCourses() {
    ProgramCourseDTO[] programs =
        (ProgramCourseDTO[]) parseResponse(fileLocation + programCoursesFile,
            ProgramCourseDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets active course audience.
   *
   * @return the active course audience
   */
  List<CourseAudienceDTO> getActiveCourseAudience() {
    CourseAudienceDTO[] courses =
        (CourseAudienceDTO[]) parseResponse(fileLocation + activeCourseAudiencesFile,
            CourseAudienceDTO[].class, false);
    return Arrays.asList(courses);
  }

  /**
   * Gets user courses.
   *
   * @return the user courses
   */
  List<UserCourseDTO> getUserCourses() {
    UserCourseDTO[] programs = (UserCourseDTO[]) parseResponse(fileLocation + userCoursesFile,
        UserCourseDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets user events.
   *
   * @return the user events
   */
  List<UserEventDTO> getUserEvents() {
    UserEventDTO[] programs =
        (UserEventDTO[]) parseResponse(fileLocation + userEventsFile, UserEventDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets active events.
   *
   * @return the active events
   */
  List<ActiveEventDTO> getActiveEvents() {
    ActiveEventDTO[] programs = (ActiveEventDTO[]) parseResponse(fileLocation + activeEventsFile,
        ActiveEventDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets user ce credits.
   *
   * @return the user ce credits
   */
  List<CeCreditsUserDTO> getUserCeCredits() {
    CeCreditsUserDTO[] programs = (CeCreditsUserDTO[]) parseResponse(fileLocation + ceCreditsFile,
        CeCreditsUserDTO[].class, false);
    return Arrays.asList(programs);
  }

  /**
   * Gets activities.
   *
   * @return the activities
   */
  List<TotaraActivityDTO> getActivities() {
    TotaraActivityDTO[] activities = (TotaraActivityDTO[]) parseResponse(fileLocation + activityFile,
        TotaraActivityDTO[].class, false);
    return Arrays.asList(activities);
  }

  /**
   * Gets activity instances.
   *
   * @return the activity instances
   */
  List<ActivityInstanceDTO> getActivityInstances() {
    ActivityInstanceDTO[] activities =
        (ActivityInstanceDTO[]) parseResponse(fileLocation + activityInstanceFile,
            ActivityInstanceDTO[].class, false);
    return Arrays.asList(activities);
  }

  /**
   * Gets user activities.
   *
   * @return the user activities
   */
  List<UserActivityDTO> getUserActivities() {
    UserActivityDTO[] activities =
        (UserActivityDTO[]) parseResponse(fileLocation + userActivitiesFile,
            UserActivityDTO[].class, false);
    return Arrays.asList(activities);
  }

  /**
   * Gets parent tags.
   *
   * @return the parent tags
   */
  List<ParentTagDTO> getParentTags() {
    ParentTagDTO[] activities =
        (ParentTagDTO[]) parseResponse(fileLocation + parentTagsFile, ParentTagDTO[].class, false);
    return Arrays.asList(activities);
  }

  /**
   * Gets skill level tags.
   *
   * @return the skill level tags
   */
  List<ProgramTagDTO> getSkillLevelTags() {
    ProgramTagDTO[] tags =
        (ProgramTagDTO[]) parseResponse(fileLocation + tagsSkillLevelFile, ProgramTagDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets language level tags.
   *
   * @return the language level tags
   */
  List<ProgramTagDTO> getLanguageLevelTags() {
    ProgramTagDTO[] tags = (ProgramTagDTO[]) parseResponse(fileLocation + tagsLanguageLevelFile,
        ProgramTagDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets role level tags.
   *
   * @return the role level tags
   */
  List<UserTagDTO> getRoleLevelTags() {
    UserTagDTO[] tags =
        (UserTagDTO[]) parseResponse(fileLocation + tagsRoleLevelFile, UserTagDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets topic level tags.
   *
   * @return the topic level tags
   */
  List<UserTagDTO> getTopicLevelTags() {
    UserTagDTO[] tags =
        (UserTagDTO[]) parseResponse(fileLocation + tagsTopicLevelFile, UserTagDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets course topic tags.
   *
   * @return the course topic tags
   */
  List<CourseTagsDTO> getCourseTopicTags() {
    CourseTagsDTO[] tags = (CourseTagsDTO[]) parseResponse(fileLocation + courseTagsTopicFile,
        CourseTagsDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets course skill tags.
   *
   * @return the course skill tags
   */
  List<CourseTagsDTO> getCourseSkillTags() {
    CourseTagsDTO[] tags = (CourseTagsDTO[]) parseResponse(fileLocation + courseTagsSkillFile,
        CourseTagsDTO[].class, false);
    return Arrays.asList(tags);
  }

  /**
   * Gets course language tags.
   *
   * @return the course language tags
   */
  List<CourseTagsDTO> getCourseLanguageTags() {
    CourseTagsDTO[] tags = (CourseTagsDTO[]) parseResponse(fileLocation + courseTagsLanguageFile,
        CourseTagsDTO[].class, false);
    return Arrays.asList(tags);
  }
}
