package com.redhat.uxl.webapp;

import com.redhat.uxl.webapp.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

/**
 * The type Application.
 */
@ComponentScan(basePackages = {
    "com.redhat.uxl.webapp",
    "com.redhat.uxl.dataobjects",
    "com.redhat.uxl.datalayer",
    "com.redhat.uxl.services"
})
@EnableAutoConfiguration
@Slf4j
public class Application {
    @Inject
    private Environment env;

    /**
     * Init application.
     *
     * @throws IOException the io exception
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT)
                    && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                log.error(
                        "You have misconfigured your application! It should not run with both the 'dev' and 'prod' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)
                    && activeProfiles.contains(Constants.SPRING_PROFILE_FAST)) {
                log.error(
                        "You have misconfigured your application! It should not run with both the 'prod' and 'fast' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT)
                    && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
                log.error(
                        "You have misconfigured your application! It should not run with both the 'dev' and 'cloud' profiles at the same time.");
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws UnknownHostException the unknown host exception
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);

        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

        // Check if the selected profile has been set as argument.
        // if not the development profile will be added
        addDefaultProfile(app, source);
        addLiquibaseScanPackages();
        Environment env = app.run(args).getEnvironment();
        log.info(
                "Access URLs:\n----------------------------------------------------------\n\t"
                        + "Local: \t\thttp://127.0.0.1:{}\n\t"
                        + "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")
                && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }

    private static void addLiquibaseScanPackages() {
        System.setProperty("liquibase.scan.packages",
                "liquibase.change,liquibase.database," + "liquibase.parser,liquibase.precondition,liquibase.datatype,"
                        + "liquibase.serializer,liquibase.sqlgenerator,liquibase.executor,"
                        + "liquibase.snapshot,liquibase.logging,liquibase.diff,"
                        + "liquibase.structure,liquibase.structurecompare,liquibase.lockservice,"
                        + "liquibase.ext,liquibase.changelog");
    }
}
