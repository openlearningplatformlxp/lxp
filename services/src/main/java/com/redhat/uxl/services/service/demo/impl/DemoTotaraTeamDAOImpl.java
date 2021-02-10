package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraTeamDAO;
import com.redhat.uxl.datalayer.dao.TotaraUserDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserWithCountsDTO;
import com.redhat.uxl.services.service.demo.dto.UserManagerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Demo totara team dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraTeamDAOImpl implements TotaraTeamDAO {

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

    /**
     * The Totara user dao.
     */
    @Inject
  TotaraUserDAO totaraUserDAO;

  @Override
  public Boolean isManager(Long userId) {
    List<UserManagerDTO> managers = demoUtilityService.getManagers();
    Boolean isManager = false;
    for (UserManagerDTO manager : managers) {
      if (manager.getManagerId().equals(userId)) {
        isManager = true;
      }
    }
    return isManager;
  }

  @Override
  public List<TotaraUserWithCountsDTO> findTeamMembersWithCountsByManager(Long managerId) {
    // TODO: Need to implement this
    TotaraUserWithCountsDTO counts = new TotaraUserWithCountsDTO();
    counts.setActivityCount(2);
    counts.setCourseCount(3);
    counts.setProgramCount(1);
    return new ArrayList<>();
  }

  @Override
  public List<TotaraProgramDTO> findTeamMembersPrograms(Set<Long> userId) {
    List<UserManagerDTO> managers = demoUtilityService.getManagers();
    Set<Long> userIds = new LinkedHashSet<>();
    for (UserManagerDTO manager : managers) {
      if (manager.getManagerId().equals(userId)) {
        userIds.add(manager.getUserId());
      }
    }
    List<TotaraProgramDTO> programList = new ArrayList<>();
    return programList; // TODO: after dao is complete.

  }

  @Override
  public List<TotaraTeamCourseDTO> findTeamMembersCourses(Set<Long> userId) {
    List<TotaraTeamCourseDTO> courses = new ArrayList<>();

    return courses; // TODO: after dao is implmeneted.
  }
}
