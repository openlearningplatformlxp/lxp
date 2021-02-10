package com.redhat.uxl.webapp.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.redhat.uxl.dataobjects.domain.util.CustomDateTimeSerializer;
import com.redhat.uxl.dataobjects.domain.util.CustomLocalDateSerializer;
import java.io.IOException;
import java.nio.charset.Charset;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;

/**
 * The type Test util.
 */
public class TestUtil {

    /**
     * The constant APPLICATION_JSON_UTF8.
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    /**
     * Convert object to json bytes byte [ ].
     *
     * @param object the object
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JodaModule module = new JodaModule();
        module.addSerializer(DateTime.class, new CustomDateTimeSerializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }

    /**
     * Create byte array byte [ ].
     *
     * @param size the size
     * @param data the data
     * @return the byte [ ]
     */
    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }
}
