package com.redhat.uxl.dataobjects.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The type Custom date time serializer.
 */
public class CustomDateTimeSerializer extends JsonSerializer<DateTime> {

  private static DateTimeFormatter formatter =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Override
  public void serialize(DateTime value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(formatter.print(value.toDateTime(DateTimeZone.UTC)));
  }

}
