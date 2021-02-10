package com.redhat.uxl.commonjava.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * The type Thread utils.
 */
@Slf4j
public final class ThreadUtils {
    private ThreadUtils() {
    }

    /**
     * Attempt to sleep long.
     *
     * @param sleepTimeInMS the sleep time in ms
     * @return the long
     */
    public static long attemptToSleep(long sleepTimeInMS) {
        long started = new Date().getTime();

        try {
            Thread.sleep(sleepTimeInMS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return new Date().getTime() - started;
    }
}
