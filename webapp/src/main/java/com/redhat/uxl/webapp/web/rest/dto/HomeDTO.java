package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.services.service.dto.AppointmentItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Home dto.
 */
@Data
public class HomeDTO implements Serializable {
    private List<AppointmentItemDTO> nextAppointments = new ArrayList<>();
    private DateTime nextAppointmentDate;
    private ProgramItemDTO currentItem;
    private BigDecimal currentItemProgression;
    private BigDecimal currentItemMinutesLeft;
    private long totalLearningPaths;
    private List<ProgramItemDTO> learningPaths = new ArrayList<>();
    private long totalPersonalLearningPaths;
    private List<ProgramItemDTO> personalLearningPaths = new ArrayList<>();
    private long totalCourses;
    private List<ProgramItemDTO> courses = new ArrayList<>();
    private long totalClassrooms;
    private List<ProgramItemDTO> classrooms = new ArrayList<>();
}
