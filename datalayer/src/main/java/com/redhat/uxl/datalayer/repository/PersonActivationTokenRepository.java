package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Person activation token repository.
 */
public interface PersonActivationTokenRepository extends BaseJpaRepository<PersonActivationToken, Long> {
    /**
     * Find all by expiry date time before list.
     *
     * @param dateTime the date time
     * @return the list
     */
    List<PersonActivationToken> findAllByExpiryDateTimeBefore(DateTime dateTime);

    /**
     * Find by person id person activation token.
     *
     * @param personId the person id
     * @return the person activation token
     */
    PersonActivationToken findByPersonId(Long personId);

    /**
     * Find by token person activation token.
     *
     * @param token the token
     * @return the person activation token
     */
    PersonActivationToken findByToken(String token);
}
