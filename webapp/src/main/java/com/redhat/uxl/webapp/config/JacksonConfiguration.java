package com.redhat.uxl.webapp.config;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.redhat.uxl.dataobjects.domain.util.CustomDateTimeDeserializer;
import com.redhat.uxl.dataobjects.domain.util.CustomDateTimeSerializer;
import com.redhat.uxl.dataobjects.domain.util.CustomLocalDateSerializer;
import com.redhat.uxl.dataobjects.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jackson configuration.
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Jackson joda module joda module.
     *
     * @return the joda module
     */
    @Bean
    public JodaModule jacksonJodaModule() {
        JodaModule module = new JodaModule();

        module.addSerializer(DateTime.class, new CustomDateTimeSerializer());
        module.addDeserializer(DateTime.class, new CustomDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        module.addDeserializer(LocalDate.class, new ISO8601LocalDateDeserializer());

        return module;
    }
}
