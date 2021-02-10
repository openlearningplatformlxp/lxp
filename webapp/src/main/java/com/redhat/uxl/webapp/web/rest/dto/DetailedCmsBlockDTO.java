package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.CmsBlock;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Detailed cms block dto.
 */
@Data
public class DetailedCmsBlockDTO extends CmsBlockDTO {
    private String createdBy;
    private DateTime createdDate;
    private String lastModifiedBy;
    private DateTime lastModifiedDate;

    /**
     * Value of detailed cms block dto.
     *
     * @param cmsBlock the cms block
     * @return the detailed cms block dto
     */
    public static DetailedCmsBlockDTO valueOf(CmsBlock cmsBlock) {
        if (cmsBlock == null) {
            return new DetailedCmsBlockDTO();
        }

        DetailedCmsBlockDTO dto = new DetailedCmsBlockDTO();

        dto.setId(cmsBlock.getId());
        dto.setKey(cmsBlock.getKey());
        dto.setName(cmsBlock.getName());
        dto.setDescription(cmsBlock.getDescription());
        dto.setContent(cmsBlock.getContent());

        dto.setCreatedBy(cmsBlock.getCreatedBy());
        dto.setCreatedDate(cmsBlock.getCreatedDate());
        dto.setLastModifiedBy(cmsBlock.getLastModifiedBy());
        dto.setLastModifiedDate(cmsBlock.getLastModifiedDate());

        return dto;
    }

    /**
     * To cms block cms block.
     *
     * @param dto the dto
     * @return the cms block
     */
    public static CmsBlock toCmsBlock(DetailedCmsBlockDTO dto) {
        if (dto == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        CmsBlock cmsBlock = new CmsBlock();

        cmsBlock.setId(dto.getId());
        cmsBlock.setKey(dto.getKey());
        cmsBlock.setName(dto.getName());
        cmsBlock.setDescription(dto.getDescription());
        cmsBlock.setContent(dto.getContent());

        return cmsBlock;
    }
}
