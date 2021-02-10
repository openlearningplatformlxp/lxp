package com.redhat.uxl.services.service.dto;

import java.util.List;
import lombok.Data;

/**
 * The type Program item wrapper dto.
 */
@Data
public class ProgramItemWrapperDTO {
    /**
     * The Max number.
     */
    int maxNumber;
    /**
     * The Total count.
     */
    long totalCount;
    /**
     * The Program items.
     */
    List<ProgramItemDTO> programItems;
    /**
     * The Archived program items.
     */
    List<ProgramItemDTO> archivedProgramItems;
    /**
     * The Shared with me items.
     */
    List<ProgramItemDTO> sharedWithMeItems;
}
