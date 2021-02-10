package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Totara file dto.
 */
@Data
public class TotaraFileDTO implements Searchable {

    /**
     * The Id.
     */
    Long id;
    /**
     * The Contextid.
     */
    Long contextid;
    /**
     * The Component.
     */
    String component;
    /**
     * The Filearea.
     */
    String filearea;
    /**
     * The Itemid.
     */
    Long itemid;
    /**
     * The Filepath.
     */
    String filepath;
    /**
     * The Filename.
     */
    String filename;
    /**
     * The Mimetype.
     */
    String mimetype;

}
