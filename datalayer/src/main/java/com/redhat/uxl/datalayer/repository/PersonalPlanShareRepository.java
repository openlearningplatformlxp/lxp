package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.types.PersonalPlanShareType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Personal plan share repository.
 */
public interface PersonalPlanShareRepository
        extends BaseJpaRepository<PersonalPlanShare, PersonalPlanShare.Pk>, JpaSpecificationExecutor<PersonalPlanShare> {

    /**
     * Find by pk personal plan id and type personal plan share.
     *
     * @param personalPlanId        the personal plan id
     * @param personalPlanShareType the personal plan share type
     * @return the personal plan share
     */
    PersonalPlanShare findByPkPersonalPlanIdAndType(Long personalPlanId, PersonalPlanShareType personalPlanShareType);

    /**
     * Find all by pk personal plan id and type list.
     *
     * @param id                    the id
     * @param personalPlanShareType the personal plan share type
     * @return the list
     */
    List<PersonalPlanShare> findAllByPkPersonalPlanIdAndType(Long id, PersonalPlanShareType personalPlanShareType);

    /**
     * Find by pk owner user id and pk shared user id and type list.
     *
     * @param ownerUserId           the owner user id
     * @param sharedUserId          the shared user id
     * @param personalPlanShareType the personal plan share type
     * @return the list
     */
    List<PersonalPlanShare> findByPkOwnerUserIdAndPkSharedUserIdAndType(Long ownerUserId, Long sharedUserId,
            PersonalPlanShareType personalPlanShareType);

    /**
     * Find all by pk personal plan id in and type list.
     *
     * @param planIds the plan ids
     * @param type    the type
     * @return the list
     */
    @Query(value = "SELECT p FROM PersonalPlanShare p WHERE p.pk.personalPlanId IN :planIds and type = :type")
    List<PersonalPlanShare> findAllByPkPersonalPlanIdInAndType(@Param("planIds") List<Long> planIds,
            @Param("type") PersonalPlanShareType type);

}
