package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Authority repository.
 */
public interface AuthorityRepository extends BaseJpaRepository<Authority, String>, JpaSpecificationExecutor<Authority> {
    /**
     * Find by name authority.
     *
     * @param name the name
     * @return the authority
     */
    Authority findByName(String name);

    /**
     * Find by name fetch permissions authority.
     *
     * @param name the name
     * @return the authority
     */
    @Query("select a from Authority a left join fetch a.permissions where a.name = (?1)")
    Authority findByNameFetchPermissions(String name);
}
