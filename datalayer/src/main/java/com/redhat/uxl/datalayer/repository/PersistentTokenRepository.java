package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.PersistentToken;
import com.redhat.uxl.dataobjects.domain.Person;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Persistent token repository.
 */
public interface PersistentTokenRepository extends BaseJpaRepository<PersistentToken, String> {

    /**
     * Find by person list.
     *
     * @param person the person
     * @return the list
     */
    List<PersistentToken> findByPerson(Person person);

    /**
     * Find by token date before list.
     *
     * @param localDate the local date
     * @return the list
     */
    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
