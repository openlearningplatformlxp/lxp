package com.redhat.uxl.webapp.config.logback;

import ch.qos.logback.classic.PatternLayout;

/**
 * The type Logback custom pattern layout.
 */
public class LogbackCustomPatternLayout extends PatternLayout {
    static {
        PatternLayout.defaultConverterMap.put("login", LogbackLoginConverter.class.getName());
    }
}
