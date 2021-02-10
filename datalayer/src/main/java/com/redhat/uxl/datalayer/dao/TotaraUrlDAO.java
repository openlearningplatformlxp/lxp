package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUrlDTO;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import java.util.List;

/**
 * The interface Totara url dao.
 */
public interface TotaraUrlDAO {

    /**
     * Find by external url path id list.
     *
     * @param learningLockerJobExecutionType the learning locker job execution type
     * @param externalUrl                    the external url
     * @return the list
     */
    List<TotaraUrlDTO> findByExternalUrlPathId(LearningLockerJobExecutionType learningLockerJobExecutionType,
            String externalUrl);
}
