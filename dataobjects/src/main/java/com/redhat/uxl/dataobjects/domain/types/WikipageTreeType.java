package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The enum Wikipage tree type.
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum WikipageTreeType {
    /**
     * Item wikipage tree type.
     */
    ITEM,
    /**
     * Folder wikipage tree type.
     */
    FOLDER;

}
