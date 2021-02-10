package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.repository.SystemMutexRepository;
import com.redhat.uxl.dataobjects.domain.SystemMutex;
import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import com.redhat.uxl.services.service.SystemMutexService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type System mutex service.
 */
@Service
@Transactional
@Slf4j
public class SystemMutexServiceImpl
    implements SystemMutexService, ApplicationListener<ContextRefreshedEvent> {
  @Inject
  private SystemMutexRepository systemMutexRepository;

  private static String system;

  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    try {
      InetAddress ip = InetAddress.getLocalHost();

      if (StringUtils.isNotBlank(ip.getHostName())
          && !"127.0.0.1".equalsIgnoreCase(ip.getHostName())) {
        system = ip.getHostName().trim();

        if (system.length() > 256) {
          system = system.substring(0, 255).toLowerCase();
        }
      }
    } catch (UnknownHostException e) {
      log.error("System Mutex fail-safe failed: error retrieving Host info.", e);
    }

    if (StringUtils.isNotBlank(system)) {
      int mutexesReleased = systemMutexRepository.releaseAllForSystem(system);

      if (mutexesReleased > 0) {
        log.warn("Mutex fail-safe released {} mutexes during startup for system {}.",
            mutexesReleased, system);
      }
    }

    return;
  }

  @Override
  @Transactional
  public boolean acquire(SystemMutexType mutexType) {
    if (mutexType == null) {
      throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR,
          "System Mutex type not specified.");
    }

    try {
      SystemMutex sm = systemMutexRepository.findByMutexTypeAndAcquiredIsFalse(mutexType);

      if (sm == null) {
        return false;
      }

      int count;

      if (StringUtils.isNotBlank(system)) {
        count = systemMutexRepository.acquire(mutexType, system);
      } else {
        count = systemMutexRepository.acquire(mutexType);
      }

      return (count > 0);
    } catch (EmptyResultDataAccessException e) {
      return false;
    } catch (PessimisticLockingFailureException e) {
      return false;
    } catch (Exception e) {
      log.error("Error acquiring System Mutex: {}", mutexType, e);
      return false;
    }
  }

  @Override
  @Transactional
  public boolean release(SystemMutexType mutexType) {
    if (mutexType == null) {
      throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR,
          "System Mutex name not specified.");
    }

    int count = systemMutexRepository.release(mutexType);

    return (count > 0);
  }
}
