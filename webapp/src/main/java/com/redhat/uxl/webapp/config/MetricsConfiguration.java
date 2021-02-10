package com.redhat.uxl.webapp.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import fr.ippon.spark.metrics.SparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * The type Metrics configuration.
 */
@Configuration
@EnableMetrics(proxyTargetClass = true)
@Profile("!" + Constants.SPRING_PROFILE_FAST)
@Slf4j
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
    private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
    private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
    private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
    private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

    @Value("${metrics.jmx.enabled:false}")
    private boolean jmxEnabled;

    private MetricRegistry metricRegistry = new MetricRegistry();

    private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        log.debug("Registering JVM gauges");
        metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
        metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));

        if (jmxEnabled) {
            log.info("Initializing Metrics JMX reporting");
            JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
            jmxReporter.start();
        }
    }

    /**
     * The type Graphite registry.
     */
    @Configuration
    @ConditionalOnClass(Graphite.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    @Slf4j
    public static class GraphiteRegistry {
        @Value("${metrics.graphite.enabled:false}")
        private boolean graphiteEnabled;

        @Value("${metrics.graphite.host}")
        private String graphiteHost;

        @Value("${metrics.graphite.port}")
        private Integer graphitePort;

        @Value("${metrics.graphite.prefix:}")
        private String graphitePrefix;

        @Inject
        private MetricRegistry metricRegistry;

        @PostConstruct
        private void init() {
            if (graphiteEnabled) {
                log.info("Initializing Metrics Graphite reporting");

                Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
                GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                        .convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS)
                        .prefixedWith(graphitePrefix).build(graphite);
                graphiteReporter.start(1, TimeUnit.MINUTES);
            }
        }
    }


    /**
     * The type Spark registry.
     */
    @Configuration
    @ConditionalOnClass(SparkReporter.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    @Slf4j
    public static class SparkRegistry {
        @Value("${metrics.spark.enabled:false}")
        private boolean sparkEnabled;

        @Value("${metrics.spark.host}")
        private String sparkHost;

        @Value("${metrics.spark.port}")
        private Integer sparkPort;

        @Value("${metrics.spark.prefix:}")
        private String sparkPrefix;

        @Inject
        private MetricRegistry metricRegistry;


        @PostConstruct
        private void init() {

            if (sparkEnabled) {
                log.info("Initializing Metrics Spark reporting");

                SparkReporter sparkReporter = SparkReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
                        .convertDurationsTo(TimeUnit.MILLISECONDS).build(sparkHost, sparkPort);
                sparkReporter.start(1, TimeUnit.MINUTES);
            }
        }
    }
}
