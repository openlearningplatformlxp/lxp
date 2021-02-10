package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.services.type.LearningLockerVerbType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * The type Learning locker filter param dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningLockerFilterParamDTO {

  private StringBuffer stringBuffer = new StringBuffer();

  private String oid;
  private DateTime timestamp;
  private List<LearningLockerVerbType> verbs;

    /**
     * Prepare.
     */
    public void prepare() {
    stringBuffer.append(
        "{\"$and\":[{\"$comment\":\"{\\\"criterionLabel\\\":\\\"A\\\",\\\"criteriaPath\\\":[\\\"lrs_id\\\"]}\","
            + "\"$or\":[{\"lrs_id\":{\"$oid\":\"");
    stringBuffer.append(oid);
    stringBuffer.append(
        "\"}}]},{\"$comment\":\"{\\\"criterionLabel\\\":\\\"B\\\",\\\"criteriaPath\\\":[\\\"timestamp\\\"]}\","
            + "\"timestamp\":{\"$gt\":{\"$dte\":\"");
    DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm'Z'");
    stringBuffer.append(dtf.print(timestamp)); // timezone format 2018-09-12T00:00Z
    stringBuffer.append(
        "\"}}},{\"$comment\":\"{\\\"criterionLabel\\\":\\\"C\\\",\\\"criteriaPath\\\":[\\\"statement\\\",\\\"verb\\\"]}\",\"$or\":[");
    boolean first = true;
    for (LearningLockerVerbType verb : verbs) {
      if (!first) {
        stringBuffer.append(",");
      }
      stringBuffer.append("{\"statement.verb.id\":\"");
      stringBuffer.append(verb.getObjectType());
      stringBuffer.append("\"}");
      first = false;
    }
    stringBuffer.append("]}]}");
  }

  public String toString() {
    return stringBuffer.toString();
  }

}
