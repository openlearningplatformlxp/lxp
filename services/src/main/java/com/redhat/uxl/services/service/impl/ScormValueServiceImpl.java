package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.repository.ScormValueRepository;
import com.redhat.uxl.dataobjects.domain.ScormValue;
import com.redhat.uxl.services.service.ScormValueService;
import com.redhat.uxl.services.service.TotaraActivityService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Scorm value service.
 */
@Service
public class ScormValueServiceImpl implements ScormValueService {

    /**
     * The Scorm value repository.
     */
    @Inject
  ScormValueRepository scormValueRepository;

    /**
     * The Totara activity service.
     */
    @Inject
  TotaraActivityService totaraActivityService;

  @Override
  public List<ScormValue> getScormValues(Long moduleId, Long personId) {
    return scormValueRepository.findByModuleIdAndPersonId(moduleId, personId);
  }

  @Transactional
  public boolean setScormValue(Long moduleId, Long personId, Long personTotaraId, String key,
      String value, boolean allowCompletion) {
    // TODO: (WJK) Revise this to manage more than one value at a time, since this involves a LOT of
    // calls.

    ScormValue scormValue =
        scormValueRepository.findByModuleIdAndPersonIdAndKey(moduleId, personId, key);

    if (scormValue == null) {
      scormValue = new ScormValue();

      scormValue.setModuleId(moduleId);
      scormValue.setKey(key);
      scormValue.setPersonId(personId);
    }

    scormValue.setValue(value);

    scormValue = scormValueRepository.save(scormValue);

    // TODO: (WJK) Handle other scorm-related requirements (i.e. requires view, specific grade
    // requirements, etc)
    // if the scorm is send the following values, mark the activity as complete:
    if (allowCompletion && scormValue.getKey().equals("cmi.core.lesson_status")
        && (scormValue.getValue().equals("passed") || scormValue.getValue().equals("completed"))) {
      totaraActivityService.completeActivity(moduleId, personTotaraId, 1);
      return true;
    }

    return false;
  }
}
