package com.redhat.uxl.dataobjects.domain;

import com.redhat.uxl.commonjava.utils.SolrUtils;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.beans.Field;
import org.jsoup.Jsoup;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.List;

import static com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO.USER_TIMEZONE;

/**
 * The type Course document.
 */
@Data
@SolrDocument(collection = "reduxl")
public class CourseDocument {
    /**
     * The constant FIELD_ID.
     */
    public static final String FIELD_ID = "id";
    /**
     * The constant FIELD_FULL_NAME.
     */
    public static final String FIELD_FULL_NAME = "fullName";
    /**
     * The constant FIELD_FULL_NAME_STRING.
     */
    public static final String FIELD_FULL_NAME_STRING = "fullNameString";
    /**
     * The constant FIELD_FULL_NAME_LOWER_STRING.
     */
    public static final String FIELD_FULL_NAME_LOWER_STRING = "fullNameLowerString";
    /**
     * The constant FIELD_SHORT_NAME.
     */
    public static final String FIELD_SHORT_NAME = "shortName";
    /**
     * The constant FIELD_DESCRIPTION.
     */
    public static final String FIELD_DESCRIPTION = "description";
    /**
     * The constant FIELD_DESCRIPTION_STRING.
     */
    public static final String FIELD_DESCRIPTION_STRING = "descriptionString";
    /**
     * The constant FIELD_DESCRIPTION_LOWER_STRING.
     */
    public static final String FIELD_DESCRIPTION_LOWER_STRING = "descriptionLowerString";
    /**
     * The constant FIELD_PROGRAM_TYPE.
     */
    public static final String FIELD_PROGRAM_TYPE = "type";
    /**
     * The constant FIELD_DELIVERY.
     */
    public static final String FIELD_DELIVERY = "delivery";
    /**
     * The constant FIELD_SKILL_LEVEL.
     */
    public static final String FIELD_SKILL_LEVEL = "skillLevel";
    /**
     * The constant FIELD_LANGUAGE.
     */
    public static final String FIELD_LANGUAGE = "language";
    /**
     * The constant FIELD_COUNTRY.
     */
    public static final String FIELD_COUNTRY = "country";
    /**
     * The constant FIELD_CITY.
     */
    public static final String FIELD_CITY = "city";
    /**
     * The constant FIELD_TAGS.
     */
    public static final String FIELD_TAGS = "tags";
    /**
     * The constant FIELD_CONTENT_SOURCE.
     */
    public static final String FIELD_CONTENT_SOURCE = "contentSource";
    /**
     * The constant FIELD_TIME_CREATED.
     */
    public static final String FIELD_TIME_CREATED = "timeCreated";
    /**
     * The constant FIELD_EVENT_TIME.
     */
    public static final String FIELD_EVENT_TIME = "eventTime";
    /**
     * The constant FIELD_SORT_ORDER.
     */
    public static final String FIELD_SORT_ORDER = "sortOrder";
    /**
     * The constant FIELD_VISIBILITY_TYPE.
     */
    public static final String FIELD_VISIBILITY_TYPE = "visibilityType";
    /**
     * The constant FIELD_ALLOWED_USERS.
     */
    public static final String FIELD_ALLOWED_USERS = "allowedUsers";

    @Id
    @Field
    @Indexed
    private String id;

    @Field
    @Indexed
    private String fullName;

    @Field
    @Indexed
    private String shortName;

    @Field
    @Indexed
    private String description;

    @Field
    @Indexed
    private ContentSourceType contentSource;

    @Field
    @Indexed
    private ProgramType type;

    @Field
    @Indexed
    private Integer delivery;

    @Field
    @Indexed
    private String skillLevel;

    @Field
    @Indexed
    private String language;

    @Field
    @Indexed
    private String firstTopic;

    @Field
    @Indexed
    private List<String> tags = new ArrayList<String>();

    @Field
    private int sortOrder = 0;

    @Field
    @Indexed
    private String fullNameString;

    @Field
    private String fullNameLowerString;

    @Field
    private String shortNameString;

    @Field
    private String descriptionString;

    @Field
    private String descriptionLowerString;

    @Field
    private String cecredits = "";

    @Field
    private String duration;

    @Field
    private String externalUrl;

    @Field
    private Long timeCreated;

    @Field
    @Indexed
    private Long eventTime;
    @Field
    @Indexed
    private String eventTimezone;
    @Field
    @Indexed
    private String city;

    @Field
    @Indexed
    private String country;

    @Field
    private String classType;

    @Field
    @Indexed
    private CourseDocumentVisibilityType visibilityType;

    @Field
    @Indexed
    private List<Long> allowedUsers = new ArrayList<>();

    /**
     * Instantiates a new Course document.
     */
    public CourseDocument() {
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public List<String> getTags() {
        if (this.tags == null)
            tags = new ArrayList<String>();

        return tags;
    }

    /**
     * Instantiates a new Course document.
     *
     * @param totaraCourseDTO the totara course dto
     */
    public CourseDocument(TotaraCourseDTO totaraCourseDTO) {
        id = ProgramType.COURSE + "{}" + String.valueOf(totaraCourseDTO.getId());
        fullName = StringUtils.lowerCase(totaraCourseDTO.getFullName());
        fullNameString = totaraCourseDTO.getFullName();
        fullNameLowerString = fullName;
        shortName = StringUtils.lowerCase(totaraCourseDTO.getShortName());
        shortNameString = totaraCourseDTO.getShortName();
        description = StringUtils.lowerCase(totaraCourseDTO.getSummary());
        descriptionString = totaraCourseDTO.getSummary();
        descriptionLowerString = description;
        contentSource = ContentSourceType.LMS;
        duration = totaraCourseDTO.getDuration();
        type = ProgramType.COURSE;
        delivery = totaraCourseDTO.getCourseType();
        timeCreated = totaraCourseDTO.getTimeCreated();
        cecredits = "" + totaraCourseDTO.getCeCredits();
        externalUrl = null;
    }

    /**
     * Instantiates a new Course document.
     *
     * @param totaraProgram the totara program
     */
    public CourseDocument(TotaraProgramDTO totaraProgram) {
        id = ProgramType.LEARNING_PATH + "{}" + totaraProgram.getProgramId();
        fullName = StringUtils.lowerCase(totaraProgram.getProgramName());
        fullNameString = totaraProgram.getProgramName();
        fullNameLowerString = fullName;
        shortName = StringUtils.lowerCase(totaraProgram.getProgramShortName());
        shortNameString = totaraProgram.getProgramShortName();
        description = StringUtils.lowerCase(totaraProgram.getProgramSummary());
        descriptionString = totaraProgram.getProgramSummary();
        descriptionLowerString = description;
        if (totaraProgram.getDuration() != null) {
            duration = String.valueOf(totaraProgram.getDuration());
        } else {
            duration = String.valueOf(0);
        }
        contentSource = ContentSourceType.LMS;
        type = ProgramType.LEARNING_PATH;
        timeCreated = totaraProgram.getTimeCreated();
        externalUrl = null;
    }

    /**
     * Instantiates a new Course document.
     *
     * @param totaraEventDTO   the totara event dto
     * @param eventExternalUrl the event external url
     */
    public CourseDocument(TotaraEventDTO totaraEventDTO, String eventExternalUrl) {
        id = ProgramType.CLASSROOM + "{}" + totaraEventDTO.getEventId();
        fullName = StringUtils.lowerCase(totaraEventDTO.getName());
        fullNameString = totaraEventDTO.getName();
        fullNameLowerString = fullName;
        shortName = StringUtils.lowerCase(totaraEventDTO.getName());
        shortNameString = totaraEventDTO.getName();
        contentSource = ContentSourceType.LMS;
        city = totaraEventDTO.getCity();
        country = totaraEventDTO.getCountry();
        classType = totaraEventDTO.getCategoryname();
        type = ProgramType.CLASSROOM;
        eventTime = totaraEventDTO.getEventTime();
        eventTimezone = totaraEventDTO.getTimezone();
        visibilityType = CourseDocumentVisibilityType.PUBLIC;
        externalUrl = eventExternalUrl;
    }

    /**
     * Instantiates a new Course document.
     *
     * @param wikipage the wikipage
     */
    public CourseDocument(Wikipage wikipage) {
        id = ProgramType.WIKIPAGE + "{}" + wikipage.getId();
        fullName = StringUtils.lowerCase(wikipage.getTitle());
        fullNameString = wikipage.getTitle();
        fullNameLowerString = fullName;
        shortName = "";
        shortNameString = "";
        contentSource = ContentSourceType.LMS;
        type = ProgramType.WIKIPAGE;
        visibilityType = CourseDocumentVisibilityType.PUBLIC;
        if (StringUtils.isNotEmpty(wikipage.getHtmlContent())) {
            String htmlText = Jsoup.parse(wikipage.getHtmlContent()).text();
            description = StringUtils.lowerCase(htmlText);
            descriptionString = htmlText;
        }
        externalUrl = wikipage.getUrl();
    }

    /**
     * Gets program id.
     *
     * @return the program id
     */
    public Long getProgramId() {
        String id = StringUtils.splitByWholeSeparator(this.id, "{}")[1];
        return Long.parseLong(id);
    }

    /**
     * Sets skill level tag.
     *
     * @param skillLevelTag the skill level tag
     */
    public void setSkillLevelTag(TotaraTagDTO skillLevelTag) {
        if (skillLevelTag != null) {
            skillLevel = skillLevelTag.getName();
        } else {
            skillLevel = null;
        }
    }

    /**
     * Sets language tag.
     *
     * @param languageTag the language tag
     */
    public void setLanguageTag(TotaraTagDTO languageTag) {
        if (languageTag != null) {
            language = languageTag.getName();
        } else {
            language = null;
        }
    }

    /**
     * Sets first topic tag.
     *
     * @param firstTopicTag the first topic tag
     */
    public void setFirstTopicTag(TotaraTagDTO firstTopicTag) {
        if (firstTopicTag != null) {
            firstTopic = firstTopicTag.getName();
        } else {
            firstTopic = null;
        }
    }

    /**
     * Sanitize strings.
     */
    public void sanitizeStrings() {
        fullName = StringUtils.lowerCase(SolrUtils.sanitizeInput(fullName).toString());
        shortName = StringUtils.lowerCase(SolrUtils.sanitizeInput(shortName).toString());
        description = StringUtils.lowerCase(SolrUtils.sanitizeInput(description).toString());
        if (description != null) {
            description = Jsoup.parse(description).text();
        }
        if (descriptionString != null) {
            descriptionString = Jsoup.parse(descriptionString).text();
        }
    }

    /**
     * Is classroom boolean.
     *
     * @return the boolean
     */
    public boolean isClassroom() {
        return ProgramType.CLASSROOM.equals(type);
    }

    /**
     * Require user timezone boolean.
     *
     * @return the boolean
     */
    public boolean requireUserTimezone() {
        return USER_TIMEZONE.equals(eventTimezone);
    }
}
