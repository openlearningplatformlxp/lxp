package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.WikipageService;
import com.redhat.uxl.services.service.dto.WikipageDTO;
import io.swagger.annotations.ApiOperation;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Wikipage resource.
 */
@RestController
@RequestMapping(value = "/api/wikipage", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class WikipageResource extends BaseResource {

    /**
     * The Wikipage service.
     */
    @Inject
    WikipageService wikipageService;

    /**
     * Gets page.
     *
     * @param url the url
     * @return the page
     */
    @ApiOperation(value = "Get wikipage", notes = "<p>Get wikipage</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<WikipageDTO> getPage(@RequestParam("url") String url) {
        log.debug("REST request to display a wikipage");
        WikipageDTO wikipage = wikipageService.displayPage(url);
        if (wikipage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<WikipageDTO>(wikipage, HttpStatus.OK);
    }

}
