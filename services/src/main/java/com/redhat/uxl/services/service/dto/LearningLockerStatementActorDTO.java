package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Learning locker statement actor dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerStatementActorDTO implements Serializable {

  private String objectType;
  private String name;
  private String mbox;

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
    String email = mbox;
    if (mbox != null) {
      email = StringUtils.replace(mbox, "mailto:", "");
    }
    return email;
  }
}
