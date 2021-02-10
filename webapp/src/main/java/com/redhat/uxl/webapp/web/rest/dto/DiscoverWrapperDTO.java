package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Discover wrapper dto.
 */
@Data
public class DiscoverWrapperDTO implements Serializable {
    private List<ProgramItemDTO> discoverPrograms = new ArrayList<>();
    private List<DiscoverInterestDTO> interests = new ArrayList<>();
    private List<DiscoverInterestDTO> roles = new ArrayList<>();
}
