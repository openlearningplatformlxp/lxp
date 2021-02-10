package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.types.FeedbackType;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Feedback dto.
 */
@Data
public class FeedbackDTO implements Serializable {
  private FeedbackType type;
  private String url;
  private String message;
}
