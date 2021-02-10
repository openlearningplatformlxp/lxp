package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Totara evidence type dto.
 */
@Data
public class TotaraEvidenceTypeDTO implements Searchable {

    /**
     * The Id.
     */
    Long id;
    /**
     * The Name.
     */
    String name;

}
