package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.CourseUpcomingClassDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course upcoming dto.
 */
@Data
public class CourseUpcomingDTO implements Serializable {

    private List<CourseUpcomingClassDTO> upcomingClassList = new ArrayList<>();

}
