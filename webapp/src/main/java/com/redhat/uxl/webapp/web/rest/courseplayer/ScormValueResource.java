package com.redhat.uxl.webapp.web.rest.courseplayer;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.ScormValue;
import com.redhat.uxl.services.service.CoursePlayerService;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.ScormValueService;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityStatusDTO;
import com.redhat.uxl.webapp.web.rest.courseplayer.dto.ScormValueOutDTO;
import com.redhat.uxl.webapp.web.rest.courseplayer.dto.ScormValueSetDTO;
import com.redhat.uxl.webapp.web.rest.courseplayer.dto.ScormValueSetResultDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The type Scorm value resource.
 */
@RestController
@RequestMapping(value = "/api/scorm", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScormValueResource {

    /**
     * The Person service.
     */
    @Inject
    PersonService personService;

    /**
     * The Scorm value service.
     */
    @Inject
    ScormValueService scormValueService;

    /**
     * The Course player service.
     */
    @Inject
    CoursePlayerService coursePlayerService;

    /**
     * Gets value.
     *
     * @param moduleId the module id
     * @return the value
     */
    @ApiOperation(value = "Get Scorm Value", notes = "<p>Get Scorm Values.</p>")
    @RequestMapping(value = "/get/{moduleId}", method = RequestMethod.GET)
    @Timed
    public ScormValueOutDTO getValue(@PathVariable Long moduleId) {

        List<ScormValue> scormValues;
        try {
            Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);
            scormValues = scormValueService.getScormValues(moduleId, person.getId());
        } catch (Exception e) {
            // TODO: (WJK) Need to handle this in some fashion
            scormValues = null;
        }

        return ScormValueOutDTO.valueOf(scormValues);

    }

    /**
     * Sets value.
     *
     * @param scormValueSetDTO the scorm value set dto
     * @param moduleId         the module id
     * @return the value
     */
    @ApiOperation(value = "Set Scorm Value", notes = "<p>Set Scorm Values.</p>")
    @RequestMapping(value = "/set/{moduleId}", method = RequestMethod.POST)
    @Timed
    public ScormValueSetResultDTO setValue(@RequestBody ScormValueSetDTO scormValueSetDTO,
            @PathVariable Long moduleId) {
        ScormValueSetResultDTO result = new ScormValueSetResultDTO();

        try {
            Long personTotaraId = SecurityUtils.getCurrentLoginAsLong();
            Person person = personService.getPersonByLogin(SecurityUtils.getCurrentLogin(), false);

            if (!SecurityUtils.isImpersonatedUser()) {
                if (scormValueService.setScormValue(moduleId, person.getId(), personTotaraId, scormValueSetDTO.getKey(),
                        scormValueSetDTO.getValue(), scormValueSetDTO.isAllowCompletion())) {
                    Map<Long, CoursePlayerActivityStatusDTO> activityStatuses = CoursePlayerActivityStatusDTO
                            .valueOfAvailable(
                                    coursePlayerService.getStatusForActivitiesInSameCourse(moduleId, personTotaraId));

                    result.setIsCompleted(true);
                    result.setActivityStatus(activityStatuses.get(moduleId));
                    result.setActivityStatuses(activityStatuses);
                }
            }

        } catch (Exception e) {
            // TODO: (WJK) Need to handle this in some fashion
        }

        return result;
    }
}
