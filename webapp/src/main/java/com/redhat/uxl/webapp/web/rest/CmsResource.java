package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.CmsBlock;
import com.redhat.uxl.services.service.CmsBlockService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.web.rest.dto.CmsBlockDTO;
import com.redhat.uxl.webapp.web.rest.dto.CmsBlocksRequestDTO;
import com.redhat.uxl.webapp.web.rest.dto.DetailedCmsBlockDTO;
import com.redhat.uxl.webapp.web.rest.dto.PagedSearchDTO;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Cms resource.
 */
@RestController
@RequestMapping(value = "/api/cms", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CmsResource extends BaseResource {
    @Inject
    private CmsBlockService cmsBlockService;

    /**
     * Add cms block detailed cms block dto.
     *
     * @param blockDTO the block dto
     * @return the detailed cms block dto
     */
    @ApiOperation(value = "Add a CMS Block.", notes = "<p>Add a CMS Block.</p>")
    @RequestMapping(value = "/block", method = RequestMethod.PUT)
    @Timed
    @RolesAllowed(value = { AuthoritiesConstants.ADMIN, AuthoritiesConstants.CMS_EDITOR })
    public DetailedCmsBlockDTO addCmsBlock(@RequestBody DetailedCmsBlockDTO blockDTO) {
        if (blockDTO == null || blockDTO.getId() != null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        CmsBlock cmsBlock = cmsBlockService.addBlock(DetailedCmsBlockDTO.toCmsBlock(blockDTO));

        return DetailedCmsBlockDTO.valueOf(cmsBlock);
    }

    /**
     * Gets block.
     *
     * @param cmsBlockId the cms block id
     * @return the block
     */
    @ApiOperation(value = "Get the CMS Block.", notes = "<p>Get the CMS Block.</p>")
    @RequestMapping(value = "/block/{cmsBlockId}", method = RequestMethod.GET)
    @Timed
    public CmsBlockDTO getBlock(@PathVariable Long cmsBlockId) {
        return CmsBlockDTO.valueOf(cmsBlockService.getBlock(cmsBlockId));
    }

    /**
     * Gets cms blocks.
     *
     * @param cmsBlocksRequestDTO the cms blocks request dto
     * @return the cms blocks
     */
    @ApiOperation(value = "Get the specified CMS Blocks.", notes = "<p>Get the specified CMS Blocks.</p>")
    @RequestMapping(value = "/blocks", method = RequestMethod.POST)
    @Timed
    public Map<String, CmsBlockDTO> getCmsBlocks(@RequestBody CmsBlocksRequestDTO cmsBlocksRequestDTO) {
        return CmsBlockDTO.valueOfMap(cmsBlockService.getBlocks(cmsBlocksRequestDTO.getKeys()));
    }

    /**
     * Search cms blocks page.
     *
     * @param pagedSearchDTO the paged search dto
     * @return the page
     */
    @ApiOperation(value = "Search for CMS Blocks.", notes = "<p>Search for CMS Blocks.</p>")
    @RequestMapping(value = "/blocks/search", method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    Page<CmsBlockDTO> searchCmsBlocks(@RequestBody PagedSearchDTO pagedSearchDTO) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "name");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "key");

        Pageable pageable = pagedSearchDTO.getPageable(order1, order2);

        Page<CmsBlock> cmsBlockPage = cmsBlockService.findForPagedSearch(pagedSearchDTO.getSearchOperation(),
                pagedSearchDTO.getSearchValue(), pageable);

        return CmsBlockDTO.valueOf(cmsBlockPage);
    }

    /**
     * Update cms block detailed cms block dto.
     *
     * @param cmsBlockId the cms block id
     * @param blockDTO   the block dto
     * @return the detailed cms block dto
     */
    @ApiOperation(value = "Update the given CMS Block.", notes = "<p>Update the given CMS Block.</p>")
    @RequestMapping(value = "/block/{cmsBlockId}", method = RequestMethod.PUT)
    @Timed
    @RolesAllowed(value = { AuthoritiesConstants.ADMIN, AuthoritiesConstants.CMS_EDITOR })
    public DetailedCmsBlockDTO updateCmsBlock(@PathVariable Long cmsBlockId,
            @RequestBody DetailedCmsBlockDTO blockDTO) {
        if (blockDTO == null || blockDTO.getId() == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        } else if (!cmsBlockId.equals(blockDTO.getId())) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
                    "Path variable id does not match body content id.");
        }

        CmsBlock cmsBlock = cmsBlockService.updateBlock(cmsBlockId, DetailedCmsBlockDTO.toCmsBlock(blockDTO));

        return DetailedCmsBlockDTO.valueOf(cmsBlock);
    }

}
