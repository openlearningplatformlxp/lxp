package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.VideoTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Video time repository.
 */
public interface VideoTimeRepository extends BaseJpaRepository<VideoTime, Long> {

    /**
     * Find by module id and person id video time.
     *
     * @param moduleId the module id
     * @param personId the person id
     * @return the video time
     */
    VideoTime findByModuleIdAndPersonId(Long moduleId, Long personId);

    /**
     * Update time.
     *
     * @param moduleId  the module id
     * @param personId  the person id
     * @param videoTime the video time
     */
    @Modifying
    @Query(value = "update video_time set time = ?3, last_modified_date = now() where person_id = ?2 and module_id = ?1", nativeQuery = true)
    void updateTime(Long moduleId, Long personId, Double videoTime);

}
