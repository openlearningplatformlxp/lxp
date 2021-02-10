package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara tag dto.
 */
@Data
public class TotaraTagDTO {

    /**
     * The Id.
     */
    Long id;
    /**
     * The Userid.
     */
    Long userid;
    /**
     * The Name.
     */
    String name;
    /**
     * The Rawname.
     */
    String rawname;
    /**
     * The Description.
     */
    String description;
    /**
     * The Flag.
     */
    Boolean flag;
    /**
     * The Item id.
     */
    Long itemId;

}
