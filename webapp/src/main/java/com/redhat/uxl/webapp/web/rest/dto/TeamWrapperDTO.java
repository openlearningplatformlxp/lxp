package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Team wrapper dto.
 */
@Data
public class TeamWrapperDTO {
    /**
     * The Team members.
     */
    List<TeamMemberDTO> teamMembers = new ArrayList<>();
    /**
     * The Progression overview.
     */
    List<TeamMemberProgressOverviewDTO> progressionOverview = new ArrayList<>();
}
