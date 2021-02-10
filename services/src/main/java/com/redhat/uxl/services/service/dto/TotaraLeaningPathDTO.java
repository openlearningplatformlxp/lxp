package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import lombok.Data;

/**
 * The type Totara leaning path dto.
 */
@Data
public class TotaraLeaningPathDTO {
    private Long userId;
    private Long courseId;
    private String programStatus;
    private String programName;
    private String programSummary;
    private String courseFullName;
    private String courseShortName;
    private String courseSummary;
    private String courseStatus;
    private String moduleName;
    private String moduleStatus;
    private Long programId;
    private Long programTrackingId;
    private Long courseTrackingId;
    private Long moduleId;
    private Long moduleTrackingId;

    /**
     * Convert to totara learning path for key value totara leaning path dto.
     *
     * @param program the program
     * @return the totara leaning path dto
     */
    public static TotaraLeaningPathDTO convertToTotaraLearningPathForKeyValue(
        TotaraProgramDTO program) {

        TotaraLeaningPathDTO dto = new TotaraLeaningPathDTO();
        dto.setProgramName(program.getProgramName());
        dto.setProgramId(program.getProgramId());
        return dto;
    }
}
