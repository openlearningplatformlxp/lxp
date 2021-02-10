package com.redhat.uxl.webapp.web.rest.dto;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

/**
 * The type Logger dto.
 */
@Data
public class LoggerDTO {

    private String name;
    private String level;

    /**
     * Instantiates a new Logger dto.
     *
     * @param logger the logger
     */
    public LoggerDTO(Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

    /**
     * Instantiates a new Logger dto.
     */
    @JsonCreator
    public LoggerDTO() {
    }

    @Override
    public String toString() {
        return "LoggerDTO{" + "name='" + name + '\'' + ", level='" + level + '\'' + '}';
    }
}
