package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraEvidenceDAO;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagInstanceDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagInstanceDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.AmazonS3Service;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.ProgramStatisticsService;
import com.redhat.uxl.services.service.dto.AchievementDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.ProfileDTO;
import com.redhat.uxl.services.service.dto.ProfileFullDTO;
import com.redhat.uxl.services.service.dto.ProfilePreferencesDTO;
import com.redhat.uxl.services.service.dto.ProfileProgressInfoDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Profile service.
 */
@Service
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {

  /**
   * The constant MAX_ACTIVITY.
   */
  public static final int MAX_ACTIVITY = 3;
  @Inject
  private AmazonS3Service amazonS3Service;

  /**
   * The Totara profile dao.
   */
  @Inject
  TotaraProfileDAO totaraProfileDAO;

  /**
   * The Totara course dao.
   */
  @Inject
  TotaraCourseDAO totaraCourseDAO;

  /**
   * The Totara tag dao.
   */
  @Inject
  TotaraTagDAO totaraTagDAO;

  /**
   * The Program statistics service.
   */
  @Inject
  ProgramStatisticsService programStatisticsService;

  /**
   * The Totara tag instance dao.
   */
  @Inject
  TotaraTagInstanceDAO totaraTagInstanceDAO;

  /**
   * The Totara evidence dao.
   */
  @Inject
  TotaraEvidenceDAO totaraEvidenceDAO;

  @Override
  @Timed
  @Transactional(readOnly = true)
  public TotaraUserDTO getTotaraUserProfile(String email) {
    return totaraProfileDAO.getUserProfileByEmail(email);

  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public TotaraUserDTO getFirstManager(Long totaraId) { // TODO: Need to allow this to work via
                                                     // authentication.
    List<TotaraUserDTO> managers = totaraProfileDAO.getUserManagers(totaraId);
    if (!managers.isEmpty()) {
      return managers.get(0);
    }
    return new TotaraUserDTO();
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public ProfileFullDTO getLoggedUserProfile(Long totaraId) { // TODO: Need to allow this to work
                                                              // via authentication.

    TotaraUserDTO tUser = totaraProfileDAO.getUserProfile(totaraId);
    List<TotaraUserDTO> managers = totaraProfileDAO.getUserManagers(totaraId);
    // Setting user base data
    ProfileFullDTO profileFullDTO = new ProfileFullDTO();
    if (tUser != null) {
      profileFullDTO.setFirstName(tUser.getFirstName());
      profileFullDTO.setLastName(tUser.getLastName());
      profileFullDTO.setCity(tUser.getCity());
      profileFullDTO.setCountry(tUser.getCountry());
      profileFullDTO.setCertId(tUser.getCertId());
      profileFullDTO.setRhdId(tUser.getRhnId());
      profileFullDTO.setTimezone(tUser.getTimezone());
      profileFullDTO.setPictureUrl(amazonS3Service.makeExternalURL("/profile-image/" + totaraId));
      profileFullDTO.setEmailAddress(tUser.getEmail());
      profileFullDTO.setLanguage(tUser.getLanguage());
    }

    // Setting user managers
    profileFullDTO.setManagers(new ArrayList<>());
    for (TotaraUserDTO manager : managers) {
      ProfileDTO manager1 = new ProfileDTO();
      manager1.setFirstName(manager.getFirstName());
      manager1.setLastName(manager.getLastName());
      manager1.setCity(manager.getCity());
      manager1.setPictureUrl(amazonS3Service.makeExternalURL("/profile-image/" + manager.getId()));
      profileFullDTO.getManagers().add(manager1);

    }
    return profileFullDTO;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public ProfileProgressInfoDTO getLoggedUserProfileProgressInfo(Long totaraId) {
    ProfileProgressInfoDTO profileProgressInfoDTO = new ProfileProgressInfoDTO();

    List<TotaraProgramDTO> userPrograms = totaraCourseDAO.findProgramsByUserId(totaraId);
    List<TotaraCourseDTO> userCourse = totaraCourseDAO.findCoursesByUserId(totaraId);
    List<TotaraActivityDTO> userActivities = totaraCourseDAO.findActivitiesByUserId(totaraId);

    profileProgressInfoDTO.setPaths(userPrograms.size());
    profileProgressInfoDTO.setCourses(userCourse.size());
    profileProgressInfoDTO.setActivities(userActivities.size());
    profileProgressInfoDTO.setCeCredits(0); // TODO: Need to find out where this is coming from

    profileProgressInfoDTO.setAchievements(new ArrayList<>());

    // Loop through all courses and programs
    for (TotaraProgramDTO tp : userPrograms) {
      AchievementDTO achievement1 = new AchievementDTO();
      achievement1.setId(tp.getProgramId());
      achievement1.setPictureUrl("http://via.placeholder.com/50x50");
      achievement1.setType(ProgramType.LEARNING_PATH);
      achievement1.setTitle(tp.getProgramName());

      LearningPathProgressionOverviewDTO overviewDTO =
          programStatisticsService.getProgramStatistics(tp.getProgramId(), totaraId);
      if (tp.getCompletedDate() != null) {
        achievement1.setCompletedDate(new DateTime(tp.getCompletedDate() * 1000));
      }
      if (tp.getTimeEnrolled() != null) {
        achievement1.setTimeEnrolled(new DateTime(tp.getTimeEnrolled() * 1000));
      }
      achievement1
          .setProgress(overviewDTO.getPercentComplete().multiply(new BigDecimal(100)).intValue());
      profileProgressInfoDTO.getAchievements().add(achievement1);
    }
    // Need to build a map for progress
    Map<Long, AchievementDTO> courseAchievementMap = new HashMap<>();
    List<Long> courseList = new ArrayList<>();

    for (TotaraCourseDTO tc : userCourse) {
      AchievementDTO achievement1 = new AchievementDTO();
      achievement1.setId(tc.getCourseId());
      achievement1.setPictureUrl("http://via.placeholder.com/50x50");
      achievement1.setType(ProgramType.COURSE);
      achievement1.setTitle(tc.getFullName());
      if (tc.getCompletedDate() != null) {
        achievement1.setCompletedDate(new DateTime(tc.getCompletedDate() * 1000));
      }
      if (tc.getTimeEnrolled() != null) {
        achievement1.setTimeEnrolled(new DateTime(tc.getTimeEnrolled() * 1000));
      }
      if (tc.getStatus() > 49) {
        achievement1.setProgress(100);

      } else {
        achievement1.setProgress(50);
      }
      courseAchievementMap.put(tc.getCourseId(), achievement1);
      courseList.add(tc.getCourseId());
      profileProgressInfoDTO.getAchievements().add(achievement1);
    }

    // Lets get all the activities for the current courses.
    List<TotaraActivityDTO> activityList = totaraCourseDAO.findActivityForUserCourses(totaraId);
    Map<Long, List<Long>> courseActivityMap = new HashMap<>();
    for (TotaraActivityDTO ta : activityList) {
      if (courseActivityMap.containsKey(ta.getCourseId())) {
        courseActivityMap.get(ta.getCourseId()).add(ta.getId());
      } else {
        List<Long> aList = new ArrayList<>();
        aList.add(ta.getId());
        courseActivityMap.put(ta.getCourseId(), aList);
      }
    }

    // Create map of user activites
    Map<Long, TotaraActivityDTO> userActivityMap = new HashMap<>();
    for (TotaraActivityDTO ta : userActivities) {
      userActivityMap.put(ta.getId(), ta);
    }

    // Loop through user courses and determine completion
    for (Long key : courseAchievementMap.keySet()) {
      AchievementDTO aDto = courseAchievementMap.get(key);

      if (aDto.getProgress() == 100) {
        // No need to do this if Totara says we are complete
        continue;
      }

      log.info("Need to determine how many activities are for course {}", key);
      List<Long> activityList2 = courseActivityMap.get(key);

      Integer numActivities = 0;
      Integer numComplete = 0;

      if (activityList2 != null) {
        log.info("There are {} activities for the course {}", activityList.size(), key);
        numActivities = activityList2.size();

        for (Long actId : activityList2) {
          if (userActivityMap.containsKey(actId)) {
            TotaraActivityDTO ta = userActivityMap.get(actId);
            log.info("Activity has a status of {}", ta.getStatus());
            if (ta.getStatus() > 0) {
              numComplete++;
            }
          }
        }
      }
      if (numActivities == 0) {
        aDto.setProgress(100);
      } else if (numComplete == 0) {
        aDto.setProgress(0);
      } else {
        log.info("Num Activites {} Num Complete {}", numActivities, numComplete);
        BigDecimal bd = new BigDecimal(numComplete)
            .divide(new BigDecimal(numActivities), 2, BigDecimal.ROUND_HALF_UP)
            .multiply(new BigDecimal(100));
        log.info("Final Amount {}", bd.toString());
        aDto.setProgress(bd.intValue());
      }
    }

    Collections.sort(profileProgressInfoDTO.getAchievements(), new Comparator<AchievementDTO>() {
      @Override
      public int compare(AchievementDTO o1, AchievementDTO o2) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });

    // Calculate CE Credits earned
    profileProgressInfoDTO.setCeCredits(totaraCourseDAO.getCECreditsForUser(totaraId));
    return profileProgressInfoDTO;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public ProfilePreferencesDTO getLoggedUserProfilePreferences(Long totaraId) {
    ProfilePreferencesDTO preferencesDTO = new ProfilePreferencesDTO();

    preferencesDTO.setPictureUrl(amazonS3Service.makeExternalURL("/profile-image/" + totaraId));

    preferencesDTO.getAllRoles().addAll(getProfileRoles(totaraId));
    preferencesDTO.getRoles().addAll(getLoggedUserRoles(totaraId));
    preferencesDTO.getInterests().addAll(getLoggedUserInterests(totaraId));

    preferencesDTO.getNotifications().setNewContentAvailable(true);
    preferencesDTO.getNotifications().setCompleteLearningPath(true);
    preferencesDTO.getNotifications().setCompleteCourse(true);
    preferencesDTO.getNotifications().setOverdueLearningPath(true);
    preferencesDTO.getNotifications().setOverdueCourse(true);

    return preferencesDTO;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public List<TagDTO> getLoggedUserInterests(Long totaraId) {
    return totaraProfileDAO.getUserInterests(totaraId).stream()
        .map(tag -> new TagDTO(tag.getId(), tag.getName())).collect(Collectors.toList());
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public List<TagDTO> getLoggedUserRoles(Long totaraId) {
    List<TotaraTagDTO> temp = totaraProfileDAO.getUserRoles(totaraId);

    List<TagDTO> temp2 = totaraProfileDAO.getUserRoles(totaraId).stream()
        .map(tag -> new TagDTO(tag.getId(), tag.getName())).collect(Collectors.toList());

    return temp2;
  }

  @Override
  public List<TagDTO> getProfileRoles(Long totaraId) {
    List<TotaraTagDTO> totaraTagDTOS = totaraTagDAO.findRoleTags();
    List<TagDTO> tagDTOS = new ArrayList<>();

    for (TotaraTagDTO t : totaraTagDTOS) {
      tagDTOS.add(new TagDTO(t.getId(), t.getName()));
    }

    return tagDTOS;
  }

  @Override
  public List<TagDTO> addProfileRole(long totaraId, TagDTO tagDTO) {

    TotaraTagInstanceDTO tagInstance = new TotaraTagInstanceDTO();
    tagInstance.setItemid(totaraId);
    tagInstance.setComponent("core");
    tagInstance.setItemType("user");
    tagInstance.setTagId(tagDTO.getId());
    tagInstance.setOrdering(0l);
    totaraTagInstanceDAO.save(tagInstance);

    return getLoggedUserRoles(totaraId);
  }

  @Override
  public List<TagDTO> getLoggedUserUnmatchedProfileInterests(Long totaraId, String searchTerm) {
    List<TotaraTagDTO> totaraTagDTOS = totaraTagDAO.findUnmatchedUserTags(totaraId, searchTerm, 5);
    List<TagDTO> tagDTOS = new ArrayList<>();

    for (TotaraTagDTO t : totaraTagDTOS) {
      tagDTOS.add(new TagDTO(t.getId(), t.getName()));
    }

    return tagDTOS;
  }

  @Override
  public List<TagDTO> getAllProfileInterests(Long totaraId) {
    List<TotaraTagDTO> totaraTagDTOS = totaraTagDAO.findAllUserTags(totaraId);
    List<TagDTO> tagDTOS = new ArrayList<>();

    for (TotaraTagDTO t : totaraTagDTOS) {
      tagDTOS.add(new TagDTO(t.getId(), t.getName()));
    }

    return tagDTOS;
  }

  @Override
  public List<TagDTO> addProfileInterest(long totaraId, TagDTO tagDTO) {

    TotaraTagInstanceDTO tagInstance = new TotaraTagInstanceDTO();
    tagInstance.setItemid(totaraId);
    tagInstance.setComponent("core");
    tagInstance.setItemType("user");
    tagInstance.setTagId(tagDTO.getId());
    tagInstance.setOrdering(0l);
    totaraTagInstanceDAO.save(tagInstance);

    return getLoggedUserInterests(totaraId);
  }

  @Override
  public void removeProfileInterest(long totaraId, long tagId) {
    totaraTagInstanceDAO.delete(totaraId, tagId);
  }

  @Override
  public void removeProfileRole(long totaraId, long tagId) {
    totaraTagInstanceDAO.delete(totaraId, tagId);
  }

  @Override
  public void uploadFile(Long currentUserId, MultipartFile file) {
    String filename = "/profile-image/" + currentUserId;
    try {
      amazonS3Service.uploadFile(file, filename);
    } catch (InterruptedException e) {
      throw new RuntimeException("Failed to upload image");
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload image");
    }
  }
}
