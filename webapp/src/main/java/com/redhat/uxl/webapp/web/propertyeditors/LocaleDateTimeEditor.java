package com.redhat.uxl.webapp.web.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

/**
 * The type Locale date time editor.
 */
public class LocaleDateTimeEditor extends PropertyEditorSupport {

    private final DateTimeFormatter formatter;

    private final boolean allowEmpty;

    /**
     * Instantiates a new Locale date time editor.
     *
     * @param dateFormat the date format
     * @param allowEmpty the allow empty
     */
    public LocaleDateTimeEditor(String dateFormat, boolean allowEmpty) {
        this.formatter = DateTimeFormat.forPattern(dateFormat);
        this.allowEmpty = allowEmpty;
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return value != null ? new LocalDateTime(value).toString(formatter) : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            setValue(new LocalDateTime(formatter.parseDateTime(text)));
        }
    }
}
