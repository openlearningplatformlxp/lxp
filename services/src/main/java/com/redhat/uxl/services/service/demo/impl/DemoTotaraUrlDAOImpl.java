package com.redhat.uxl.services.service.demo.impl;

import com.redhat.uxl.datalayer.dao.TotaraUrlDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUrlDTO;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Demo totara url dao.
 */
@Slf4j
@Service
@ConditionalOnExpression("${demo.mode.enabled:true}")
public class DemoTotaraUrlDAOImpl implements TotaraUrlDAO {

    @Override
  public List<TotaraUrlDTO> findByExternalUrlPathId(
      LearningLockerJobExecutionType learningLockerJobExecutionType, String externalUrl) {
    return null;
  }

}
