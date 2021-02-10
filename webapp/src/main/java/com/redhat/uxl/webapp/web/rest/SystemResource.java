package com.redhat.uxl.webapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.HttpUtils;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.dao.SystemDAO;
import com.redhat.uxl.services.service.CmsBlockService;
import com.redhat.uxl.webapp.security.AuthoritiesConstants;
import com.redhat.uxl.webapp.security.SecurityUtils;
import com.redhat.uxl.webapp.web.rest.dto.CmsBlockDTO;
import io.swagger.annotations.ApiOperation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type System resource.
 */
@RestController
@RequestMapping(value = "/api/system", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class SystemResource extends BaseResource {
    /**
     * The Env.
     */
    @Autowired
    Environment env;

    @Value("${app.analytics.googleAnalytics.domainName}")
    private String appAnalyticsGoogleAnalyticsDomainName;

    @Value("${app.analytics.googleAnalytics.trackingId}")
    private String appAnalyticsGoogleAnalyticsTrackingId;

    private List<String> appHealthCheckExternalUrls = Collections.synchronizedList(new ArrayList<>());

    @Value("${app.security.client.session.timeout.idleSecsBeforeWarning}")
    private String appSecurityClientSessionTimeoutIdleSecsBeforeWarning;

    @Value("${app.security.client.session.timeout.secsBeforeLogout}")
    private String appSecurityClientSessionTimeoutSecsBeforeLogout;

    @Value("${app.config.supporturl}")
    private String appSupportURL;

    @Value("${totara.course.baseurl}")
    private String totaraCourseLaunchUrl;

    @Value("${totara.classroom.baseurl}")
    private String totaraClassroomLaunchUrl;

    @Value("${saml.redirect.enabled}")
    private Boolean samlRedirectEnabled;

    @Value("${saml.redirect.url}")
    private String samlRedirectUrl;

    /**
     * Sets app health check external urls.
     *
     * @param urls the urls
     */
    @Value("${app.healthCheck.externalUrls}")
    public void setAppHealthCheckExternalUrls(String[] urls) {
        if (urls != null) {
            for (String url : urls) {
                if (StrUtils.isNotBlank(url)) {
                    appHealthCheckExternalUrls.add(url.trim());
                }
            }
        }
    }

    @Inject
    private CmsBlockService cmsBlockService;

    @Inject
    private SystemDAO systemDAO;

    /**
     * Gets bootstrap config.
     *
     * @param appConfig the app config
     * @return the bootstrap config
     */
    @ApiOperation(value = "Get Bootstrap config", notes = "<p>Get Bootstrap config (i.e. any config need before angular bootstrapping occurs).</p>")
    @RequestMapping(value = "/bootstrap/config", method = RequestMethod.POST)
    @Timed
    public Map<String, Object> getBootstrapConfig(@RequestBody Map<String, String> appConfig) {
        Map<String, Object> config = new HashMap<>(3);

        try {
            // Add bootstrap config items to config map.
            config.put("googleAnalytics.trackingId", appAnalyticsGoogleAnalyticsTrackingId);
            config.put("googleAnalytics.domainName", appAnalyticsGoogleAnalyticsDomainName);
            config.put("session.timeout.idleSecsBeforeWarning", appSecurityClientSessionTimeoutIdleSecsBeforeWarning);
            config.put("session.timeout.secsBeforeLogout", appSecurityClientSessionTimeoutSecsBeforeLogout);
            config.put("app.config.supporturl", appSupportURL);
            config.put("totara.course.baseurl", totaraCourseLaunchUrl);
            config.put("totara.classroom.baseurl", totaraClassroomLaunchUrl);
            config.put("saml.redirect.enabled", samlRedirectEnabled);
            config.put("saml.redirect.url", samlRedirectUrl);

            config.put("blocks", CmsBlockDTO.valueOfMap(cmsBlockService.getAllCurrentBlocks()));
        } catch (Exception e) {
            log.error("Error loading bootstrap config.", e);
        }

        return config;
    }

    /**
     * Gets health check.
     *
     * @param response the response
     * @return the health check
     */
    @ApiOperation(value = "Health Check.", notes = "<p>Health Check.</p>")
    @RequestMapping(value = "/health-check", method = RequestMethod.GET)
    @Timed
    public List<Object> getHealthCheck(HttpServletResponse response) {
        List<Object> values = new ArrayList<>(1);
        Map<String, String> value = new HashMap<>();

        value.put("time", DateTime.now().toString());
        values.add(value);

        value = new HashMap<>();

        try {
            InetAddress ip = InetAddress.getLocalHost();

            value.put("server", ip.getHostName());
        } catch (UnknownHostException e) {
            value.put("server", "local");
            value.put("status-message", "Unable to retrieve host name.");
        }

        try {
            systemDAO.healthCheck();
            value.put("status", "ok");
        } catch (Exception e) {
            value.put("status", "error");
            value.put("status-message", "DB error.");

            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        values.add(value);

        if (appHealthCheckExternalUrls != null && appHealthCheckExternalUrls.size() > 0) {
            for (String url : appHealthCheckExternalUrls) {
                Map<String, Object> serverResponse = HttpUtils.get(url, false);
                value = new HashMap<>();

                value.put("server", url);

                if ((boolean) serverResponse.get("success")) {
                    value.put("status", "ok");
                } else {
                    value.put("status", "error");
                    value.put("status-message", (String) serverResponse.get("message"));

                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }

                values.add(value);
            }
        }

        return values;
    }

    /**
     * Info map.
     *
     * @param request the request
     * @return the map
     */
    @ApiOperation(value = "System Info.", notes = "<p>System Info.</p>")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @Timed
    public Map<String, Map<String, String>> info(HttpServletRequest request) {
        Map<String, Map<String, String>> allInfo = new HashMap<>();
        Map<String, String> generalInfo = new HashMap<>(1);
        Map<String, String> headerInfo = new HashMap<>();
        Map<String, String> propertyInfo = new HashMap<>();

        allInfo.put("general", generalInfo);
        allInfo.put("header", headerInfo);
        allInfo.put("property", propertyInfo);

        // General Info

        try {
            InetAddress ip = InetAddress.getLocalHost();

            generalInfo.put("server.hostname", ip.getHostName());

            if (!SecurityUtils.hasGrantedAuthority(AuthoritiesConstants.ADMIN)) {
                return allInfo;
            }

            generalInfo.put("server.ip", ip.getHostAddress());
        } catch (UnknownHostException e) {
            log.error("Error retrieving Host info.", e);
        }

        generalInfo.put("request.localAddr", request.getLocalAddr());
        generalInfo.put("request.localName", request.getLocalName());
        generalInfo.put("request.localPort", Integer.valueOf(request.getLocalPort()).toString());

        generalInfo.put("request.remoteAddr", request.getRemoteAddr());
        generalInfo.put("request.remoteHost", request.getRemoteHost());
        generalInfo.put("request.remotePort", Integer.valueOf(request.getRemotePort()).toString());

        generalInfo.put("request.serverName", request.getServerName());
        generalInfo.put("request.serverPort", Integer.valueOf(request.getServerPort()).toString());

        // Header Info

        for (Enumeration nameEnum = request.getHeaderNames(); nameEnum.hasMoreElements();) {
            String headerName = (String) nameEnum.nextElement();
            StringBuilder value = new StringBuilder();

            for (Enumeration valueEnum = request.getHeaders(headerName); valueEnum.hasMoreElements();) {
                if (value.length() > 0) {
                    value.append(", ");
                }

                value.append(valueEnum.nextElement());
            }

            headerInfo.put(headerName, value.toString());
        }

        // Environment Property Info

        for (Iterator propertySourcesIter = ((AbstractEnvironment) env).getPropertySources()
                .iterator(); propertySourcesIter.hasNext();) {
            PropertySource propertySource = (PropertySource) propertySourcesIter.next();

            if (propertySource instanceof MapPropertySource) {
                Map<String, Object> source = ((MapPropertySource) propertySource).getSource();

                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    propertyInfo.put(entry.getKey(), entry.getValue().toString());
                }
            }
        }

        return allInfo;
    }
}
