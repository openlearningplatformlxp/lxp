package com.redhat.uxl.datalayer.solr.repository;

import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.List;
import java.util.Set;

/**
 * The interface Course document custom solr repository.
 */
public interface CourseDocumentCustomSolrRepository {

    /**
     * Find by criteria highlight page.
     *
     * @param userId                     the user id
     * @param featuredSearchInstanceList the featured search instance list
     * @param searchTerm                 the search term
     * @param programTypeList            the program type list
     * @param contentSourceTypeList      the content source type list
     * @param topic                      the topic
     * @param delivery                   the delivery
     * @param skillLevels                the skill levels
     * @param languages                  the languages
     * @param country                    the country
     * @param city                       the city
     * @param isAdminSearch              the is admin search
     * @param pageable                   the pageable
     * @return the highlight page
     */
    HighlightPage<CourseDocument> findByCriteria(Long userId, Set<FeaturedSearchInstance> featuredSearchInstanceList,
            StringSolrBuffer searchTerm, List<ProgramType> programTypeList,
            List<ContentSourceType> contentSourceTypeList, String topic, List<Integer> delivery, String skillLevels,
            String languages, String country, String city, boolean isAdminSearch, Pageable pageable);
}
