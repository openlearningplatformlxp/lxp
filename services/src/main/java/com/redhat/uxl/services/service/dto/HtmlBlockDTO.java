package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.dto.TotaraHtmlBlockDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Html block dto.
 */
@Data
@AllArgsConstructor
public class HtmlBlockDTO {

    private String title;
    private String htmlContent;

    /**
     * Instantiates a new Html block dto.
     */
    public HtmlBlockDTO() {
    }

    /**
     * Instantiates a new Html block dto.
     *
     * @param htmlBlock the html block
     */
    public HtmlBlockDTO(TotaraHtmlBlockDTO htmlBlock) {
        title = htmlBlock.getTitle();
        htmlContent = htmlBlock.getHtmlContent();
    }

}
