package com.redhat.uxl.commonjava.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Http utils.
 */
public final class HttpUtils {
    private HttpUtils() {
    }

    /**
     * Get map.
     *
     * @param urlString  the url string
     * @param getContent the get content
     * @return the map
     */
    public static Map<String, Object> get(String urlString, boolean getContent) {
        BufferedReader in = null;
        Map<String, Object> result = new HashMap<>();

        result.put("url", urlString);
        result.put("success", false);

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

            result.put("responseCode", httpUrlConnection.getResponseCode());

            if (getContent) {
                String inputLine;
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            }

            result.put("success", true);
        } catch (MalformedURLException e) {
            result.put("message", e.getMessage());
        } catch (IOException e) {
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("message", e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
