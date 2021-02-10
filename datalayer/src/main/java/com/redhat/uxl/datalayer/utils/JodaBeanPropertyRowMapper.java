package com.redhat.uxl.datalayer.utils;

import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * The type Joda bean property row mapper.
 *
 * @param <T> the type parameter
 */
public class JodaBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
    /**
     * Instantiates a new Joda bean property row mapper.
     *
     * @param mappedClass the mapped class
     */
    public JodaBeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    @Override
    protected void initBeanWrapper(BeanWrapper beanWrapper) {
        beanWrapper.registerCustomEditor(DateTime.class, new JodaDateTimeEditor());
    }
}
