package com.redhat.uxl.datalayer.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

/**
 * The type Abstract base totara dao.
 */
@Slf4j
public abstract class AbstractBaseTotaraDAOImpl {

    /**
     * The Webservice base url.
     */
    @Value("${totara.webservice.baseurl}")
    protected String WEBSERVICE_BASE_URL;

    /**
     * The Course token.
     */
    @Value("${totara.webservice.token}")
    protected String COURSE_TOKEN;

    /**
     * The Rest template timeout.
     */
    protected int REST_TEMPLATE_TIMEOUT = 100;
    /**
     * The Rest template.
     */
    protected RestTemplate restTemplate;

    /**
     * Instantiates a new Abstract base totara dao.
     */
    public AbstractBaseTotaraDAOImpl() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        requestFactory.setReadTimeout((1000 * 60) * REST_TEMPLATE_TIMEOUT);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    /**
     * To string long list string.
     *
     * @param numbers the numbers
     * @return the string
     */
    public String toStringLongList(List<Long> numbers) {
        String idsString = "";
        Iterator<Long> iterator = numbers.iterator();
        while (iterator.hasNext()) {
            Long id = iterator.next();
            idsString += id.toString();
            if (iterator.hasNext()) {
                idsString += ",";
            }
        }
        return idsString;
    }

    /**
     * Generate url string.
     *
     * @param token    the token
     * @param function the function
     * @param params   the params
     * @return the string
     */
    public String generateURL(String token, String function, String params) {
        String url = WEBSERVICE_BASE_URL + "?wstoken=" + token + function + params + "&moodlewsrestformat=json";
        log.debug("url for " + function + ": " + url);
        return url;
    }
}
