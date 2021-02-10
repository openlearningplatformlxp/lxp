package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.AllegoContentRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Allego content recipient repository.
 */
public interface AllegoContentRecipientRepository extends JpaRepository<AllegoContentRecipient, Long> {
    /**
     * Find by content id list.
     *
     * @param contentId the content id
     * @return the list
     */
    List<AllegoContentRecipient> findByContentId(Long contentId);
}
