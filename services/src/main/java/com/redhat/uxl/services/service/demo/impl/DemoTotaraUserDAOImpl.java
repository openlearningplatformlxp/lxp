package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraUserDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.demo.dto.UserProgramDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The type Demo totara user dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraUserDAOImpl implements TotaraUserDAO {

    /**
     * The Demo utility service.
     */
    @Inject DemoUtilityService demoUtilityService;

  @Override
  public List<TotaraUserDTO> findAllActiveUsers() {
    return demoUtilityService.getUsers();
  }

  @Override
  public List<TotaraUserDTO> findAllActiveUsersInProgram(Long programId) {
    List<UserProgramDTO> programUserList = demoUtilityService.getUserPrograms();
    Set<Long> userIds = new TreeSet<>();
    for (UserProgramDTO dto : programUserList) {
      if (programId.equals(dto.getProgramId())) {
        userIds.add(dto.getUserId());
      }
    }
    return findUsersByIds(userIds);
  }

  @Override
  public List<TotaraUserDTO> findUsersByIds(Set<Long> userIdList) {
    List<TotaraUserDTO> allUsers = demoUtilityService.getUsers();
    List<TotaraUserDTO> foundUsers = new ArrayList<>();
    for (Long uId : userIdList) {
      for (TotaraUserDTO user : allUsers) {
        if (user.getId().equals(uId)) {
          foundUsers.add(user);
        }
      }
    }
    return foundUsers;
  }

  @Override
  public TotaraUserDTO findUserById(Long userId) {
    List<TotaraUserDTO> users = demoUtilityService.getUsers();
    for (TotaraUserDTO user : users) {
      if (user.getId().equals(userId)) {
        return user;
      }
    }
    return null;
  }

}
