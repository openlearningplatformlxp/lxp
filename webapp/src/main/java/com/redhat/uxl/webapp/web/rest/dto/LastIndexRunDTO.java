package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Last index run dto.
 */
@Data
public class LastIndexRunDTO implements Serializable {
    private long time;
    private boolean indexEnabled = true;
}
