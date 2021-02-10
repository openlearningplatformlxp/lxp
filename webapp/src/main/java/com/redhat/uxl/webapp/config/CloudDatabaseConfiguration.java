package com.redhat.uxl.webapp.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The type Cloud database configuration.
 */
@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
@Slf4j
public class CloudDatabaseConfiguration extends AbstractCloudConfig {
    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        log.info("Configuring JDBC datasource from a cloud provider");
        return connectionFactory().dataSource();
    }
}
