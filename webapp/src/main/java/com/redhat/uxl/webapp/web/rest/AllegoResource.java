package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.utils.SSLUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Allego resource.
 */
@RestController
@RequestMapping(value = "/api/allego", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AllegoResource extends BaseResource {

    /**
     * The Recipient url.
     */
    @Value("${app.allego.api.recipient.url}")
    protected String recipientUrl;
    /**
     * The Search url.
     */
    @Value("${app.allego.api.search.url}")
    protected String searchUrl;
    /**
     * The Auth key.
     */
    @Value("${app.allego.api.key.auth}")
    protected String authKey;

    /**
     * Allego recipients string.
     *
     * @param from the from
     * @param size the size
     * @return the string
     */
    @ApiOperation(value = "Get content recipients in allego", notes = "<p>Get content recipients in allego. Useful to bypass the cloudfront proxy</p>")
    @RequestMapping(value = "/recipients", method = RequestMethod.GET)
    @Timed
    public String allegoRecipients(@RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        return doAllegoCall(recipientUrl, from, size);
    }

    /**
     * Allego search string.
     *
     * @param from the from
     * @param size the size
     * @return the string
     */
    @ApiOperation(value = "Search in allego", notes = "<p>Search in allego. Useful to bypass the cloudfront proxy</p>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Timed
    public String allegoSearch(@RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {

        log.info("Enabling SNI");
        SSLUtils.enableSNI();
        try {
            log.info("Trying Search Allego call");
            return doAllegoCall(searchUrl, from, size);
        } finally {
            log.info("Finally Disabling SNI");
            SSLUtils.disableSNI();
            ;
        }
    }

    /**
     * Allego search no sni string.
     *
     * @param from the from
     * @param size the size
     * @return the string
     */
    @ApiOperation(value = "Search in allego", notes = "<p>Search in allego. Useful to bypass the cloudfront proxy</p>")
    @RequestMapping(value = "/search/nosni", method = RequestMethod.GET)
    @Timed
    public String allegoSearchNoSni(@RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {

        log.info("Disabling SNI");
        SSLUtils.disableSNI();
        log.info("Trying Search Allego call");
        return doAllegoCall(searchUrl, from, size);
    }

    private String doAllegoCall(String url, Integer from, Integer size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("from", from).queryParam("size",
                size);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Allego " + authKey);
        HttpEntity<String> entity = new HttpEntity(null, headers);
        final ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                String.class);
        return response.getBody();
    }

}
