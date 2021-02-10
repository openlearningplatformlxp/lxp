package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.repository.DatabaseSettingsRepository;
import com.redhat.uxl.dataobjects.domain.DatabaseSettings;
import com.redhat.uxl.services.service.DatabaseSettingsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Database settings service.
 */
@Service
@Slf4j
@Transactional
public class DatabaseSettingsServiceImpl implements DatabaseSettingsService {

  @Inject
  private DatabaseSettingsRepository databaseSettingsRepository;
  private Map<String, String> settingsMap = new HashMap<>();

    /**
     * Read settings.
     */
    @PostConstruct
  public void readSettings() {
    List<DatabaseSettings> settings = databaseSettingsRepository.findAll();
    settings.stream().forEach(s -> settingsMap.put(s.getName(), s.getValue()));
  }

  @Override
  public String findValue(String name) {
    return settingsMap.get(name);
  }

  @Override
  public Integer findIntValue(String name) {
    return Integer.parseInt(findValue(name));
  }
}
