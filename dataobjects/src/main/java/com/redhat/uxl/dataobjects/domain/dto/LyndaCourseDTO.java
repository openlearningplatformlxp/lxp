package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Lynda course dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyndaCourseDTO implements Searchable {

  private static final String LEVEL = "Level";
  private static final String TOPIC = "Topic";
  @JsonProperty("ID")
  private Long id;
  @JsonProperty("DurationInSeconds")
  private Long durationInSeconds;
  @JsonProperty("Title")
  private String title;
  @JsonProperty("ShortDescription")
  private String shortDescription;
  @JsonProperty("Description")
  private String description;
  @JsonProperty("URLs")
  private LyndaCourseURLDTO url;
  @JsonProperty("Tags")
  private List<LyndaCourseTagDTO> tags;
  @JsonProperty("DateOriginallyReleasedUtc")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
  private Date timeCreated;

    /**
     * Gets tags as string.
     *
     * @return the tags as string
     */
    public List<String> getTagsAsString() {
    if (tags != null) {
      return tags.stream().filter(tag -> !LEVEL.equals(tag.getType())).map(tag -> tag.getName())
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

    /**
     * Gets first topic.
     *
     * @return the first topic
     */
    public String getFirstTopic() {
    if (tags != null) {
      for (LyndaCourseTagDTO tag : tags) {
        if (TOPIC.equals(tag.getType())) {
          return tag.getName();
        }
      }
    }
    return null;
  }

    /**
     * Gets skill level.
     *
     * @return the skill level
     */
    public String getSkillLevel() {
    if (tags != null) {
      for (LyndaCourseTagDTO tag : tags) {
        if (LEVEL.equals(tag.getType())) {
          return tag.getName();
        }
      }
    }
    return null;
  }
}
