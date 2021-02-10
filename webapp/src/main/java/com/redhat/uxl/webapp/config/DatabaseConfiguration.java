package com.redhat.uxl.webapp.config;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * The type Database configuration.
 */
@Configuration
@EnableJpaRepositories({ "com.redhat.uxl.datalayer.repository" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EntityScan("com.redhat.uxl.dataobjects.domain")
@Slf4j
public class DatabaseConfiguration {
    @Inject
    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Value("${spring.datasource.url:#{null}}")
    private String url;

    @Value("${spring.datasource.databaseName:#{null}}")
    private String databaseName;

    @Value("${spring.datasource.dataSourceClassName}")
    private String dataSourceClassName;

    @Value("${spring.datasource.serverName}")
    private String serverName;

    @Value("${spring.datasource.port}")
    private String port;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.maximumPoolSize:10}")
    private int maximumPoolSize;

    @Value("${spring.totara.datasource.databaseName:#{null}}")
    private String totaraDatabaseName;

    @Value("${spring.totara.datasource.dataSourceClassName}")
    private String totaraDataSourceClassName;

    @Value("${spring.totara.datasource.serverName}")
    private String totaraServerName;

    @Value("${spring.totara.datasource.username}")
    private String totaraUsername;

    @Value("${spring.totara.datasource.password}")
    private String totaraPassword;

    @Value("${spring.totara.datasource.url:#{null}}")
    private String totaraUrl;

    @Value("${spring.totara.datasource.maximumPoolSize:10}")
    private int totaraMaximumPoolSize;

    @Value("${liquibase.context}")
    private String liquibaseContext;

    @Value("${liquibase.startup.shouldRun:false}")
    private Boolean liquibaseShouldRun;

    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean(destroyMethod = "close")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('cloud')}")
    @Primary
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        if (url == null && databaseName == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceClassName);
        if (StrUtils.isBlank(url)) {
            config.addDataSourceProperty("databaseName", databaseName);
            config.addDataSourceProperty("serverName", serverName);
            config.addDataSourceProperty("portNumber", port);
        } else {
            config.setJdbcUrl(url);
            config.addDataSourceProperty("jdbcUrl", url);
        }
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.setMaximumPoolSize(maximumPoolSize);

        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }

    /**
     * Totara data source data source.
     *
     * @return the data source
     */
    @Bean(destroyMethod = "close")
    public DataSource totaraDataSource() {
        log.debug("Configuring Totara Datasource");
        if (totaraUrl == null && totaraDatabaseName == null) {
            log.error("Your Totara database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Totara database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(totaraDataSourceClassName);
        if (StrUtils.isBlank(totaraUrl)) {
            config.addDataSourceProperty("databaseName", totaraDatabaseName);
            config.addDataSourceProperty("serverName", totaraServerName);
        } else {
            config.setJdbcUrl(totaraUrl);
            config.addDataSourceProperty("jdbcUrl", totaraUrl);
        }
        config.addDataSourceProperty("user", totaraUsername);
        config.addDataSourceProperty("password", totaraPassword);
        config.setMaximumPoolSize(totaraMaximumPoolSize);

        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }


    /**
     * Liquibase spring liquibase.
     *
     * @param dataSource the data source
     * @return the spring liquibase
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseContext);
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
            if ("org.h2.jdbcx.JdbcDataSource".equals(dataSourceClassName)) {
                liquibase.setShouldRun(true);
                log.warn("Using '{}' profile with H2 database in memory is not optimal, you should consider switching to" +
                    " MySQL or Postgresql to avoid rebuilding your database upon each start.", Constants.SPRING_PROFILE_FAST);
            } else {
                liquibase.setShouldRun(false);
            }
        } else {
            if (liquibaseShouldRun) {
                log.debug("Configuring Liquibase");
            } else {
                liquibase.setShouldRun(false);
            }
        }
        return liquibase;
    }

    /**
     * Hibernate 5 module hibernate 5 module.
     *
     * @return the hibernate 5 module
     */
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /**
     * Jdbc template jdbc template.
     *
     * @return the jdbc template
     */
    @Bean
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Totara jdbc template jdbc template.
     *
     * @return the jdbc template
     */
    @Bean
    @Qualifier("totaraJdbcTemplate")
    public JdbcTemplate totaraJdbcTemplate() {
        return new JdbcTemplate(totaraDataSource());
    }

}
