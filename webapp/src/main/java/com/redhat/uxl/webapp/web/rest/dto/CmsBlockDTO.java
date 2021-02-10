package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.CmsBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * The type Cms block dto.
 */
@Data
public class CmsBlockDTO {
    private Long id;

    private String key;
    private String name;
    private String description;

    private String content;

    /**
     * Value of cms block dto.
     *
     * @param cmsBlock the cms block
     * @return the cms block dto
     */
    public static CmsBlockDTO valueOf(CmsBlock cmsBlock) {
        if (cmsBlock == null) {
            return new CmsBlockDTO();
        }

        CmsBlockDTO dto = new CmsBlockDTO();

        dto.setId(cmsBlock.getId());
        dto.setKey(cmsBlock.getKey());
        dto.setName(cmsBlock.getName());
        dto.setDescription(cmsBlock.getDescription());
        dto.setContent(cmsBlock.getContent());

        return dto;
    }

    /**
     * Value of map map.
     *
     * @param cmsBlocks the cms blocks
     * @return the map
     */
    public static Map<String, CmsBlockDTO> valueOfMap(Set<CmsBlock> cmsBlocks) {
        if (cmsBlocks == null || cmsBlocks.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, CmsBlockDTO> blocksMap = new HashMap<>(cmsBlocks.size());

        for (CmsBlock block : cmsBlocks) {
            blocksMap.put(block.getKey(), CmsBlockDTO.valueOf(block));
        }

        return blocksMap;
    }

    /**
     * Value of page.
     *
     * @param cmsBlockPage the cms block page
     * @return the page
     */
    public static Page<CmsBlockDTO> valueOf(Page<CmsBlock> cmsBlockPage) {
        if (cmsBlockPage == null || !cmsBlockPage.hasContent()) {
            return new PageImpl<>(new ArrayList<>(0));
        }

        List<CmsBlockDTO> dtos = new ArrayList<>(cmsBlockPage.getNumberOfElements());

        for (CmsBlock block : cmsBlockPage.getContent()) {
            dtos.add(CmsBlockDTO.valueOf(block));
        }

        Pageable pageable = PageRequest.of(cmsBlockPage.getNumber(), cmsBlockPage.getSize(), cmsBlockPage.getSort());

        return new PageImpl<>(dtos, pageable, cmsBlockPage.getTotalElements());
    }

    // Private Helper Methods

}
