package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The enum Asset store type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AssetStoreType {
    /**
     * Db asset store type.
     */
    DB,
    /**
     * S 3 asset store type.
     */
    S3;

}
