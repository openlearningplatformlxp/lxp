package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.TagsService;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.CatalogDTO;
import com.redhat.uxl.webapp.web.rest.dto.ParentTagDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Catalog resource.
 */
@RestController
@RequestMapping(value = "/api/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CatalogResource extends BaseResource {

    @Inject
    private TagsService tagsService;
    @Inject
    private ProgramItemService programItemService;

    /**
     * Gets catalog types.
     *
     * @return the catalog types
     */
    @ApiOperation(value = "Get catalog types", notes = "<p>Get info for catalog dropdown.</p>")
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @Timed
    public List<TagDTO> getCatalogTypes() {
        log.debug("REST request to get catalog data");

        List<TagDTO> tagDTOS = tagsService.findParentTags();

        return tagDTOS;
    }

    /**
     * Gets child catalog types.
     *
     * @param parentTagId the parent tag id
     * @return the child catalog types
     */
    @ApiOperation(value = "Get type", notes = "<p>Get info for catalog type dropdown.</p>")
    @RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
    @Timed
    public ParentTagDTO getChildCatalogTypes(@PathVariable("id") Long parentTagId) {
        log.debug("REST request to get catalog data");
        TagDTO parentTag = tagsService.findParentTag(parentTagId);
        List<TagDTO> tagDTOS = tagsService.findChildTags(parentTagId);
        tagDTOS = tagDTOS.stream().filter(tagDTO -> StringUtils.isNotEmpty(tagDTO.getName()))
                .collect(Collectors.toList());
        return new ParentTagDTO(parentTag, tagDTOS);
    }

    /**
     * Gets catalog data.
     *
     * @param tagId the tag id
     * @return the catalog data
     */
    @ApiOperation(value = "Get catalog data", notes = "<p>Get info for catalog page.</p>")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Timed
    public CatalogDTO getCatalogData(@PathVariable("id") Long tagId) {
        log.debug("REST request to get catalog data");

        CatalogDTO catalogDTO = new CatalogDTO();

        TagDTO tagDTO = tagsService.findTag(tagId);

        if (tagDTO != null) {
            Long currentUserId = SecurityUtils.getCurrentLoginAsLong();
            catalogDTO.setName(tagDTO.getName());
            catalogDTO.setDescription(
                    "Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in.");
            catalogDTO.setLearningPaths(programItemService.findProgramsByTag(currentUserId, tagId));
            catalogDTO.setCourses(programItemService.findCoursesByTag(currentUserId, tagId));
        }

        return catalogDTO;
    }

}
