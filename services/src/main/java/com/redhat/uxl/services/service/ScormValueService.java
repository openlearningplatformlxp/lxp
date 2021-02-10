package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.ScormValue;
import java.util.List;

/**
 * The interface Scorm value service.
 */
public interface ScormValueService {

    /**
     * Gets scorm values.
     *
     * @param moduleId the module id
     * @param personId the person id
     * @return the scorm values
     */
    List<ScormValue> getScormValues(Long moduleId, Long personId);

    /**
     * Sets scorm value.
     *
     * @param moduleId        the module id
     * @param personId        the person id
     * @param personTotaraId  the person totara id
     * @param key             the key
     * @param value           the value
     * @param allowCompletion the allow completion
     * @return the scorm value
     */
    boolean setScormValue(Long moduleId, Long personId, Long personTotaraId, String key, String value,
            boolean allowCompletion);

}
