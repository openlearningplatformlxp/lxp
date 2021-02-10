package com.redhat.uxl.commonjava.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The type System utils.
 */
@Slf4j
public final class SystemUtils {
  private SystemUtils() {
  }

    /**
     * Gets host name.
     *
     * @return the host name
     */
    public static String getHostName() {
    try {
      InetAddress ip = InetAddress.getLocalHost();

      return ip.getHostName();
    } catch (UnknownHostException e) {
      log.warn("Error retrieving Host info.", e);

      return "";
    }
  }
}
