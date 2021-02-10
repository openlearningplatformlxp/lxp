package com.redhat.uxl.services.utils;

import com.redhat.uxl.commonjava.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Ssl utils.
 */
@Slf4j
public class SSLUtils {
    /**
     * Disable sni.
     */
    public static void disableSNI() {
    System.setProperty("jsse.enableSNIExtension", "false");
    ThreadUtils.attemptToSleep(1000);
  }

    /**
     * Enable sni.
     */
    public static void enableSNI() {
    System.setProperty("jsse.enableSNIExtension", "true");
    ThreadUtils.attemptToSleep(1000);
  }
}
