package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraTagInstanceDTO;

/**
 * The interface Totara tag instance dao.
 */
public interface TotaraTagInstanceDAO {
    /**
     * Save.
     *
     * @param totaraTagInstanceDTO the totara tag instance dto
     */
    void save(TotaraTagInstanceDTO totaraTagInstanceDTO);

    /**
     * Delete.
     *
     * @param totaraId the totara id
     * @param tagId    the tag id
     */
    void delete(long totaraId, long tagId);

    /**
     * Delete wiki id.
     *
     * @param wikiId the wiki id
     * @param tagId  the tag id
     */
    void deleteWikiId(long wikiId, long tagId);
}
