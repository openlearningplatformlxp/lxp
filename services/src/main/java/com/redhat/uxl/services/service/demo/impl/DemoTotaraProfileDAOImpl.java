package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraAudienceDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFeedbackDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.demo.dto.UserAudienceDTO;
import com.redhat.uxl.services.service.demo.dto.UserInterestsDTO;
import com.redhat.uxl.services.service.demo.dto.UserManagerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Demo totara profile dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraProfileDAOImpl implements TotaraProfileDAO {

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

  @Override
  public TotaraUserDTO getUserProfile(Long totaraUserId) {
    List<TotaraUserDTO> users = demoUtilityService.getUsers();
    for (TotaraUserDTO user : users) {
      if (user.getId().equals(totaraUserId)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public List<TotaraUserDTO> getUserManagers(Long totaraUserId) {
    List<UserManagerDTO> users = demoUtilityService.getManagers();
    List<TotaraUserDTO> managers = new ArrayList<>();
    for (UserManagerDTO user : users) {
      if (user.getUserId().equals(totaraUserId)) {
        managers.add(getUserProfile(user.getManagerId()));
      }
    }
    return managers;
  }

  @Override
  public List<TotaraAudienceDTO> getUserAudiences(Long totaraUserId) {
    List<TotaraAudienceDTO> audiences = demoUtilityService.getAudiences();
    List<UserAudienceDTO> userAudienceList = demoUtilityService.getUserAudiences();
    List<Long> uAudiences = new ArrayList<>();
    for (UserAudienceDTO audienceDTO : userAudienceList) {
      if (totaraUserId.equals(audienceDTO.getUserId())) {
        uAudiences.add(audienceDTO.getAudienceId());
      }
    }
    if (uAudiences.isEmpty()) {
      return new ArrayList<>();
    }
    List<TotaraAudienceDTO> retList = new ArrayList<>();
    for (Long id : uAudiences) {
      for (TotaraAudienceDTO audience : audiences) {
        if (audience.getId().equals(id)) {
          retList.add(audience);
        }
      }
    }
    return retList;
  }

  @Override
  public List<TotaraTagDTO> getUserInterests(Long totaraUserId) {
    List<TotaraTagDTO> allTags = demoUtilityService.getTags();
    List<UserInterestsDTO> userTags = demoUtilityService.getUserInterests();
    List<TotaraTagDTO> myTags = new ArrayList<>();
    for (UserInterestsDTO myTag : userTags) {
      for (TotaraTagDTO tag : allTags) {
        if (myTag.getTagId().equals(tag.getId())) {
          tag.setUserid(totaraUserId);
          myTags.add(tag);
        }
      }
    }
    return myTags;
  }

  @Override
  public TotaraUserDTO getUserProfileByEmail(String email) {
    List<TotaraUserDTO> users = demoUtilityService.getUsers();
    for (TotaraUserDTO user : users) {
      if (user.getEmail().equals(email)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public List<TotaraTagDTO> getUserRoles(Long totaraUserId) {
    List<TotaraTagDTO> allTags = demoUtilityService.getRoles();
    List<UserInterestsDTO> userTags = demoUtilityService.getUserRoles();
    List<TotaraTagDTO> myTags = new ArrayList<>();
    for (UserInterestsDTO myTag : userTags) {
      for (TotaraTagDTO tag : allTags) {
        if (myTag.getTagId().equals(tag.getId())) {
          tag.setUserid(totaraUserId);
          myTags.add(tag);
        }
      }
    }
    return myTags;
  }

  @Override
  public TotaraFeedbackDTO getUserProfileFieldForFeedback(Long totaraUserId) {
    List<TotaraFeedbackDTO> feedback = demoUtilityService.getUserFeedback();
    for (TotaraFeedbackDTO feebackDto : feedback) {
      if (feebackDto.getId().equals(totaraUserId)) {
        return feebackDto;
      }
    }
    return null;
  }
}
