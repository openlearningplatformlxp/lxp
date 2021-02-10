package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Profile dto.
 */
@Data
public class ProfileDTO implements Serializable {

    /**
     * The Picture url.
     */
    protected String pictureUrl;
    /**
     * The First name.
     */
    protected String firstName;
    /**
     * The Last name.
     */
    protected String lastName;
    /**
     * The City.
     */
    protected String city;
    /**
     * The State.
     */
    protected String state;
    /**
     * The Country.
     */
    protected String country;
    /**
     * The Cert id.
     */
    protected String certId;
    /**
     * The Rhd id.
     */
    protected String rhdId;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    @JsonProperty
  public String getDisplayName() {
    return StringUtils.join(
        new String[] {StringUtils.capitalize(firstName), StringUtils.capitalize(lastName)}, " ");
  }
}
