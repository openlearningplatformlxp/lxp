package com.redhat.uxl.commonjava.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Object utils.
 */
@Slf4j
public final class ObjectUtils {
    private ObjectUtils() {
    }

    /**
     * To json string.
     *
     * @param dtoObject the dto object
     * @return the string
     */
    public static String toJSON(Object dtoObject) {
        try {
            return ObjectUtils.toJSON(dtoObject, false);
        } catch (Exception e) {
            log.error("Unable to convert object to JSON: {}", dtoObject);
            return null;
        }
    }

    /**
     * To json string.
     *
     * @param dto          the dto
     * @param throwOnError the throw on error
     * @return the string
     * @throws JsonProcessingException the json processing exception
     */
    public static String toJSON(Object dto, boolean throwOnError) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(dto);
    }
}
