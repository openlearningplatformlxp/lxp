package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.services.utils.SSLUtils;
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

/**
 * The type Abstract base totara service.
 */
@Slf4j
public abstract class AbstractBaseTotaraServiceImpl {

    @Value("${totara.webservice.baseurl}")
    private String WEBSERVICE_BASE_URL;

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
     * The Params.
     */
    protected String params;

    /**
     * Instantiates a new Abstract base totara service.
     */
    public AbstractBaseTotaraServiceImpl() {
        SSLUtils.disableSNI();

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

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        requestFactory.setReadTimeout((1000 * 60) * REST_TEMPLATE_TIMEOUT);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

}
