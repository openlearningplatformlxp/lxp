package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Person announcement repository.
 */
public interface PersonAnnouncementRepository
    extends BaseJpaRepository<PersonAnnouncement, Long>, JpaSpecificationExecutor<PersonAnnouncement> {

}
