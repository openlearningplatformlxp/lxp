package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.dataobjects.domain.types.WikipageStatusType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Wikipage repository.
 */
public interface WikipageRepository extends BaseJpaRepository<Wikipage, Long>, JpaSpecificationExecutor<Wikipage> {

    /**
     * Find by url and status wikipage.
     *
     * @param url    the url
     * @param status the status
     * @return the wikipage
     */
    Wikipage findByUrlAndStatus(String url, WikipageStatusType status);

    /**
     * Find published wikipages list.
     *
     * @return the list
     */
    @Query("select w from Wikipage w where w.status = 'PUBLISHED' order by w.url desc")
    List<Wikipage> findPublishedWikipages();

    /**
     * Find published and indexed wikipages page.
     *
     * @param page the page
     * @return the page
     */
    @Query("select w from Wikipage w where w.status = 'PUBLISHED' and w.indexOnSearch = true order by w.createdDate desc")
    Page<Wikipage> findPublishedAndIndexedWikipages(Pageable page);

    /**
     * Count published and indexed wikipages long.
     *
     * @return the long
     */
    @Query("select count(w) from Wikipage w where w.status = 'PUBLISHED' and w.indexOnSearch = true order by w.createdDate desc")
    Long countPublishedAndIndexedWikipages();

}
