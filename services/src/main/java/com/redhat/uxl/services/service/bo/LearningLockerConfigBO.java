package com.redhat.uxl.services.service.bo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * The type Learning locker config bo.
 */
@Data
@Service
public class LearningLockerConfigBO implements Serializable {

  @Value("${app.learninglocker.api.base}")
  private String appLearningLockerApiBase;

  @Value("${app.learninglocker.api.auth}")
  private String appLearningLockerApiAuth;

  @Value("${app.learninglocker.api.version}")
  private String appLearningLockerApiVersion;

  @Value("${app.learninglocker.lynda.id}")
  private String appLearningLockerLyndaId;

  @Value("${app.learninglocker.lynda.oid}")
  private String appLearningLockerLyndaOid;

  @Value("${app.learninglocker.allego.id}")
  private String appLearningLockerAllegoId;

  @Value("${app.learninglocker.allego.oid}")
  private String appLearningLockerAllegoOid;

  @Value("${app.learninglocker.kaltura.id}")
  private String appLearningLockerKalturaId;

  @Value("${app.learninglocker.kaltura.oid}")
  private String appLearningLockerKalturaOid;

    /**
     * Gets statement url.
     *
     * @return the statement url
     */
    public String getStatementUrl() {
    return appLearningLockerApiBase;
  }

    /**
     * Gets auth headers.
     *
     * @return the auth headers
     */
    public HttpHeaders getAuthHeaders() {
    return new HttpHeaders() {
      {
        String authHeader = "Basic " + getAppLearningLockerApiAuth();
        set("Authorization", authHeader);
        set("X-Experience-API-Version", appLearningLockerApiVersion);
      }
    };
  }
}
