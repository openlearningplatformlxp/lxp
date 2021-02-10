package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.services.service.bo.LearningLockerConfigBO;
import com.redhat.uxl.services.service.dto.LearningLockerFilterParamDTO;
import com.redhat.uxl.services.service.dto.LearningLockerResponseDTO;
import com.redhat.uxl.services.type.LearningLockerVerbType;
import org.joda.time.DateTime;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * The type Learning locker statement.
 */
@Service
public class LearningLockerStatementImpl {

    private RestTemplate restTemplate = new RestTemplate();

    @Inject
    private LearningLockerConfigBO learningLockerConfigBO;

    /**
     * Gets paged kultura statements.
     *
     * @param timestamp the timestamp
     * @param cursor    the cursor
     * @return the paged kultura statements
     */
    public LearningLockerResponseDTO getPagedKulturaStatements(DateTime timestamp, String cursor) {
        LearningLockerFilterParamDTO filterParam = new LearningLockerFilterParamDTO();
        filterParam.setOid(learningLockerConfigBO.getAppLearningLockerKalturaOid());
        filterParam.setTimestamp(timestamp);
        filterParam.setVerbs(Arrays.asList(LearningLockerVerbType.WATCHED, LearningLockerVerbType.PLAYED));
        filterParam.prepare();
        return getStatements(filterParam.toString(), cursor);
    }

    /**
     * Gets paged lynda statements.
     *
     * @param timestamp the timestamp
     * @param cursor    the cursor
     * @return the paged lynda statements
     */
    public LearningLockerResponseDTO getPagedLyndaStatements(DateTime timestamp, String cursor) {
        LearningLockerFilterParamDTO filterParam = new LearningLockerFilterParamDTO();
        filterParam.setOid(learningLockerConfigBO.getAppLearningLockerLyndaOid());
        filterParam.setTimestamp(timestamp);
        filterParam.setVerbs(Arrays.asList(LearningLockerVerbType.COMPLETED, LearningLockerVerbType.VIEWED));
        filterParam.prepare();
        return getStatements(filterParam.toString(), cursor);
    }

    /**
     * Gets paged allego statements.
     *
     * @param timestamp the timestamp
     * @param cursor    the cursor
     * @return the paged allego statements
     */
    public LearningLockerResponseDTO getPagedAllegoStatements(DateTime timestamp, String cursor) {
        LearningLockerFilterParamDTO filterParam = new LearningLockerFilterParamDTO();
        filterParam.setOid(learningLockerConfigBO.getAppLearningLockerAllegoOid());
        filterParam.setTimestamp(timestamp);
        filterParam
                .setVerbs(Arrays.asList(LearningLockerVerbType.ALLEGO_COMPLETED, LearningLockerVerbType.ALLEGO_SCORED));
        filterParam.prepare();
        return getStatements(filterParam.toString(), cursor);
    }

    private LearningLockerResponseDTO getStatements(String filterValue, String cursor) {
        String url = learningLockerConfigBO.getStatementUrl();
        String params = "?sort={sort}&filter={filter}&after={after}";
        String urlWithParams = url + params;
        String sortValue = "{\"timestamp\":-1,\"_id\":1}";
        ResponseEntity<LearningLockerResponseDTO> response = restTemplate.exchange(urlWithParams, HttpMethod.GET,
                new HttpEntity(learningLockerConfigBO.getAuthHeaders()), LearningLockerResponseDTO.class, sortValue,
                filterValue, cursor);
        LearningLockerResponseDTO learningLockerResponseDTO = response.getBody();
        return learningLockerResponseDTO;
    }
}
