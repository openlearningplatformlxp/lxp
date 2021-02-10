package com.redhat.uxl.services.service.dto;

import com.kaltura.client.types.KalturaMediaEntry;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Solr kaltura media entry searchable dto.
 */
@Data
public class SolrKalturaMediaEntrySearchableDTO extends KalturaMediaEntry implements Searchable {
}
