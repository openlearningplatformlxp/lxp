package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.CmsBlock;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Cms block service.
 */
public interface CmsBlockService {
    /**
     * Add block cms block.
     *
     * @param cmsBlock the cms block
     * @return the cms block
     */
    CmsBlock addBlock(CmsBlock cmsBlock);

    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<CmsBlock> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Gets all current blocks.
     *
     * @return the all current blocks
     */
    Set<CmsBlock> getAllCurrentBlocks();

    /**
     * Gets block.
     *
     * @param id the id
     * @return the block
     */
    CmsBlock getBlock(Long id);

    /**
     * Gets blocks.
     *
     * @param keys the keys
     * @return the blocks
     */
    Set<CmsBlock> getBlocks(Set<String> keys);

    /**
     * Update block cms block.
     *
     * @param cmsBlockId the cms block id
     * @param cmsBlock   the cms block
     * @return the cms block
     */
    CmsBlock updateBlock(Long cmsBlockId, CmsBlock cmsBlock);
}
