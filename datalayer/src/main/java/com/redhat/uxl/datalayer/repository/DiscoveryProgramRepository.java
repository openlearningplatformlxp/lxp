package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Discovery program repository.
 */
public interface DiscoveryProgramRepository
        extends BaseJpaRepository<DiscoveryProgram, Long>, JpaSpecificationExecutor<Person> {

    /**
     * Find by active list.
     *
     * @param maxSize the max size
     * @return the list
     */
    @Query(value = "select d.* from discovery_programs d \n" + "           where d.active = true \n"
            + "           limit ?", nativeQuery = true)
    List<DiscoveryProgram> findByActive(int maxSize);
}
