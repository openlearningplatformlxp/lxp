package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Person repository.
 */
public interface PersonRepository extends BaseJpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    /**
     * Delete int.
     *
     * @param personIds the person ids
     * @return the int
     */
    @Modifying
    @Query("update Person p set p.deleted = true where p.deleted = false and p.id in (?1)")
    int delete(List<Long> personIds);

    /**
     * Find one by email ignore case and deleted is false person.
     *
     * @param email the email
     * @return the person
     */
    Person findOneByEmailIgnoreCaseAndDeletedIsFalse(String email);

    /**
     * Find one by login ignore case and deleted is false person.
     *
     * @param login the login
     * @return the person
     */
    Person findOneByLoginIgnoreCaseAndDeletedIsFalse(String login);

    /**
     * Find all users list.
     *
     * @return the list
     */
    @Query("select p from Person p where p.deleted = false")
    List<Person> findAllUsers();

    /**
     * Find by email person.
     *
     * @param email the email
     * @return the person
     */
    @Query("select p from Person p where p.email = ?1 and p.deleted = false")
    Person findByEmail(String email);

    /**
     * Find one deleted is false person.
     *
     * @param personId the person id
     * @return the person
     */
    @Query("select p from Person p where p.id = ?1 and p.deleted = false")
    Person findOneDeletedIsFalse(Long personId);
}
