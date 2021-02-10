package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.dto.SearchFilterDTO;
import com.redhat.uxl.services.service.dto.TagDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Tags service.
 */
public interface TagsService {

    /**
     * Gets search filter values.
     *
     * @return the search filter values
     */
    @Timed
    @Transactional(readOnly = true)
    SearchFilterDTO getSearchFilterValues();

    /**
     * Find parent tags list.
     *
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> findParentTags();

    /**
     * Find child tags list.
     *
     * @param tagId the tag id
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TagDTO> findChildTags(Long tagId);

    /**
     * Find tag tag dto.
     *
     * @param tagId the tag id
     * @return the tag dto
     */
    @Timed
    @Transactional(readOnly = true)
    TagDTO findTag(Long tagId);

    /**
     * Find parent tag tag dto.
     *
     * @param parentTagId the parent tag id
     * @return the tag dto
     */
    @Timed
    @Transactional(readOnly = true)
    TagDTO findParentTag(Long parentTagId);
}
