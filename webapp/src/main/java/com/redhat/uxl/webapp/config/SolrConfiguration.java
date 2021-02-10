package com.redhat.uxl.webapp.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.net.MalformedURLException;

/**
 * The type Solr configuration.
 */
@Configuration
@EnableSolrRepositories(basePackages = "com.redhat.uxl.datalayer.solr")
@Slf4j
public class SolrConfiguration implements EnvironmentAware {

    private static final String PROPERTY_NAME_SOLR_SERVER_URL = "spring.data.solr.host";
    private static final String PROPERTY_NAME_SOLR_SERVER_CORE = "spring.data.solr.core";
    private Environment environment;

    /**
     * Solr client solr client.
     *
     * @return the solr client
     * @throws MalformedURLException the malformed url exception
     * @throws IllegalStateException the illegal state exception
     */
    @Bean
    public SolrClient solrClient() throws MalformedURLException, IllegalStateException {
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder();
        String baseSolrUrl = environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_URL);
        builder.withBaseSolrUrl(baseSolrUrl);

        HttpSolrClient httpSolrClient = builder.build();

        return httpSolrClient;
    }

    /**
     * Solr template solr template.
     *
     * @return the solr template
     * @throws Exception the exception
     */
    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        SolrTemplate solrTemplate = new SolrTemplate(solrClient());
        return solrTemplate;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
