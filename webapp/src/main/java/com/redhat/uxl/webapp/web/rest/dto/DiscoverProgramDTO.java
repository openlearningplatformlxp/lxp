package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Discover program dto.
 */
@Data
public class DiscoverProgramDTO implements Serializable {
    private Long id;
    private Long programId;
    private Boolean active = false;
    private DiscoveryProgramType type;
    private String discoveryProgramText;

    /**
     * Value of discover program dto.
     *
     * @param dp the dp
     * @return the discover program dto
     */
    public static DiscoverProgramDTO valueOf(DiscoveryProgram dp) {
        DiscoverProgramDTO dto = new DiscoverProgramDTO();
        dto.setId(dp.getId());
        dto.setActive(dp.isActive());
        dto.setProgramId(dp.getProgramId());
        dto.setType(dp.getType());
        dto.setDiscoveryProgramText(dp.getDiscoveryProgramText());
        return dto;
    }

}
