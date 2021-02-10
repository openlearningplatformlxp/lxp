package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import com.redhat.uxl.dataobjects.domain.types.CalendarType;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The type Program item dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramItemDTO implements Serializable, Comparable<ProgramItemDTO> {

    private Long id;
    private Long sessionId;
    private String externalId;
    private String title;
    private String description;
    private String firstTopic;
    private ProgramType type;
    private CalendarType classType;
    private ContentSourceType cms;
    private boolean mobile;
    private boolean personal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date dueDate;
    private String externalUrl;
    private Float duration;
    private String eventTimezone;
    private String hidesets;
    private boolean enrolled;
    private boolean enrolSelfEnabled;
    private String ceCredits;
    // For search classes
    private String city;
    private String country;
    private DateTime eventTime;
    private Integer audienceVisible;
    private String courseType;
    private Long timeEnrolled;
    private Integer status;
    private boolean featured;

    // for discovery program
    private DiscoveryProgramType discoveryProgramType;
    private String discoveryProgramText;

    // for Search Autocomlpete
    private String imageType;

    // For personal learning paths
    private boolean archived;
    private long accountId;
    private long ownerId;
    private boolean shared;
    private String manager;
    private boolean sharedWithManager;
    private DateTime sharedWithManagerOn;
    private boolean sharedWithReports;
    private List<PersonalPlanShare> shares;

    /**
     * Instantiates a new Program item dto.
     *
     * @param discoveryProgram the discovery program
     */
    public ProgramItemDTO(DiscoveryProgram discoveryProgram) {
        setDiscoveryProgramType(discoveryProgram.getType());
        setDiscoveryProgramText(discoveryProgram.getDiscoveryProgramText());
    }

    /**
     * Instantiates a new Program item dto.
     *
     * @param tp the tp
     */
    public ProgramItemDTO(TotaraProgramDTO tp) {
        setTitle(StringUtils.trimToEmpty(tp.getProgramName()));
        setDescription(tp.getProgramSummary());
        setAudienceVisible(tp.getAudienceVisible());
        setType(ProgramType.LEARNING_PATH);
        if (tp.getDueDate() != null && tp.getDueDate() > 0) {
            setDueDate(new DateTime(tp.getDueDate() * 1000).toDate());
        }
        setPersonal(false);
        setId(tp.getProgramId());
        if (tp.getDuration() != null) {
            setDuration(tp.getDuration());
        } else {
            setDuration(0f);
        }
        setEnrolSelfEnabled(tp.isEnrolSelfEnabled());
        setHidesets(tp.getHidesets());
        cms = ContentSourceType.LMS;
    }

    /**
     * Instantiates a new Program item dto.
     *
     * @param pp the pp
     */
    public ProgramItemDTO(PersonalProgramDTO pp) {
        this((TotaraProgramDTO) pp);
        this.shared = true;
    }

    /**
     * Instantiates a new Program item dto.
     *
     * @param tc the tc
     */
    public ProgramItemDTO(TotaraTeamCourseDTO tc) {
        setId(tc.getCourse());
        setTitle(tc.getFullName());
        setDescription(tc.getDescription());
        setType(ProgramType.COURSE);
        setCourseType(tc.getType());
        setTimeEnrolled(tc.getTimeEnrolled());
    }

    /**
     * Instantiates a new Program item dto.
     *
     * @param tc the tc
     */
    public ProgramItemDTO(TotaraCourseDTO tc) {
        setTitle(tc.getFullName());
        setDescription(tc.getSummary());
        setType(ProgramType.COURSE);
        if (tc.getDueDate() != null && tc.getDueDate() > 0) {
            setDueDate(new DateTime(tc.getDueDate() * 1000).toDate());
        }
        setDuration(NumberUtils.toFloat(tc.getDuration()));
        setPersonal(false);

        // TODO: (WJK) This is a bandaid for missing ids on pages. The "courseId" field winds up used for Home page
        // and "id" winds up used for Discover page. Determine whether or not there are really supposed to be
        // two fields used here.
        Long courseId = tc.getCourseId();
        if (courseId == null) {
            courseId = tc.getId();
        }
        setId(courseId);
        setCeCredits("" + tc.getCeCredits());
        cms = ContentSourceType.LMS;
    }

    /**
     * Instantiates a new Program item dto.
     *
     * @param te               the te
     * @param eventExternalUrl the event external url
     */
    public ProgramItemDTO(TotaraEventDTO te, String eventExternalUrl) {
        setTitle(te.getName());
        setType(ProgramType.CLASSROOM);
        setPersonal(false);
        setId(te.getEventId());
        setSessionId(te.getSessionId());
        if (te.getDuration() != null) {
            setDuration(Float.valueOf(te.getDuration()));
        } else {
            setDuration(0f);
        }
        setClassType(CalendarType.buildFrom(te.getCategoryname()));
        setCity(te.getCity());
        setCountry(te.getCountry());
        setEventTime(new DateTime(te.getEventTime()));
        setEventTimezone(te.getTimezone());
        cms = ContentSourceType.LMS;
        setExternalUrl(eventExternalUrl);
    }

    @Override
    public int compareTo(ProgramItemDTO o) {
        return this.id.compareTo(o.id);
    }

    /**
     * Value of program item dto.
     *
     * @param p the p
     * @return the program item dto
     */
    public static ProgramItemDTO valueOf(PersonalProgramDTO p) {
        return ProgramItemDTO.builder().id(p.getProgramId()).title(p.getProgramName())
                .description(p.getProgramSummary()).type(ProgramType.LEARNING_PATH).cms(ContentSourceType.LMS)
                .dueDate(p.getProgramDueDate() != null ? new DateTime(p.getProgramDueDate()).toDate() : null)
                .firstTopic("Ansible") // Todo find real tag
                .personal(true).build();
    }
}
