package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Person password reset token repository.
 */
public interface PersonPasswordResetTokenRepository extends BaseJpaRepository<PersonPasswordResetToken, Long> {
    /**
     * Find all by expiry date time before list.
     *
     * @param dateTime the date time
     * @return the list
     */
    List<PersonPasswordResetToken> findAllByExpiryDateTimeBefore(DateTime dateTime);

    /**
     * Find by person id person password reset token.
     *
     * @param id the id
     * @return the person password reset token
     */
    PersonPasswordResetToken findByPersonId(Long id);

    /**
     * Find by token person password reset token.
     *
     * @param token the token
     * @return the person password reset token
     */
    PersonPasswordResetToken findByToken(String token);
}
