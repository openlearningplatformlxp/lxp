package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.Wikipage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Wikipage dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikipageDTO extends Wikipage {

    private List<TagDTO> tags;

    /**
     * Instantiates a new Wikipage dto.
     *
     * @param wikipage the wikipage
     */
    public WikipageDTO(Wikipage wikipage) {
        setId(wikipage.getId());
        setTitle(wikipage.getTitle());
        setUrl(wikipage.getUrl());
        setCssContent(wikipage.getCssContent());
        setHtmlContent(wikipage.getHtmlContent());
        setStatus(wikipage.getStatus());
        setIndexOnSearch(wikipage.getIndexOnSearch());
    }
}
