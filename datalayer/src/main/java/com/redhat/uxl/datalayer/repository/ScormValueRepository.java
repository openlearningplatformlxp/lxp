package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.ScormValue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Scorm value repository.
 */
public interface ScormValueRepository extends BaseJpaRepository<ScormValue, Long> {

    /**
     * Find by module id and person id list.
     *
     * @param moduleId the module id
     * @param personId the person id
     * @return the list
     */
    List<ScormValue> findByModuleIdAndPersonId(Long moduleId, Long personId);

    /**
     * Find by module id and person id and key scorm value.
     *
     * @param moduleId the module id
     * @param personId the person id
     * @param key      the key
     * @return the scorm value
     */
    ScormValue findByModuleIdAndPersonIdAndKey(Long moduleId, Long personId, String key);

}
