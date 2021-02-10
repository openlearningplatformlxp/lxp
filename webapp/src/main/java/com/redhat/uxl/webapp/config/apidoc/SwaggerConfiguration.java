package com.redhat.uxl.webapp.config.apidoc;

import com.redhat.uxl.webapp.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Date;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * The type Swagger configuration.
 */
@Configuration
@EnableSwagger2
@Profile("!" + Constants.SPRING_PROFILE_FAST)
@Slf4j
public class SwaggerConfiguration {
    @Value("${swagger.includePatterns}")
    private String swaggerIncludePatterns;

    @Value("${swagger.title}")
    private String swaggerTitle;

    @Value("${swagger.description}")
    private String swaggerDescription;

    @Value("${swagger.version}")
    private String swaggerVersion;

    @Value("${swagger.termsOfServiceUrl}")
    private String swaggerTermsOfServiceUrl;

    @Value("${swagger.contact}")
    private String swaggerContact;

    @Value("${swagger.license}")
    private String swaggerLicense;

    @Value("${swagger.licenseUrl}")
    private String swaggerLicenseUrl;

    @Value("${swagger.apiDocsLocation}")
    private String swaggerApiDocsLocation;


    /**
     * Swagger springfox docket docket.
     *
     * @return the docket
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class).forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(org.joda.time.LocalDate.class, String.class)
                .directModelSubstitute(org.joda.time.LocalDateTime.class, Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDate.class, String.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class).select()
                .paths(regex(swaggerIncludePatterns)).build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(swaggerTitle,
                swaggerDescription,
                swaggerVersion,
                swaggerTermsOfServiceUrl,
                new Contact(swaggerContact, "", ""),
                swaggerLicense,
                swaggerLicenseUrl,
                new ArrayList());
    }
}
