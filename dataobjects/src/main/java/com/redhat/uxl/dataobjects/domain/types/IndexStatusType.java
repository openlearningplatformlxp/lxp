package com.redhat.uxl.dataobjects.domain.types;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The enum Index status type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IndexStatusType {
    /**
     * Waiting index status type.
     */
    WAITING,
    /**
     * Started index status type.
     */
    STARTED,
}
