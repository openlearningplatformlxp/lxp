package com.redhat.uxl.webapp.web.controller;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.commonjava.utils.SystemUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

/**
 * The type Asset storage controller.
 */
@ConditionalOnProperty("app.assets.storage.enabled")
@Controller
@RequestMapping(value = "/asset-storage")
@Slf4j
public class AssetStorageController extends AbstractBaseController {
    private static final int MAX_READ_SIZE = 64000;

    @Value("${app.assets.storage.storageDirectory}")
    private String appAssetsProxyStorageDirectory;

    /**
     * Proxy asset.
     *
     * @param request  the request
     * @param response the response
     */
    @RequestMapping(value = "/asset/**")
    public final void proxyAsset(HttpServletRequest request, HttpServletResponse response) {
        String urlString = makeUrl(request.getContextPath(),
                (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

        if (StrUtils.isBlank(urlString)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        String filePath = urlString.substring(6);
        File file = new File(appAssetsProxyStorageDirectory + filePath);
        boolean cached = file.exists() && !file.isDirectory();

        if (cached && StrUtils.isNotBlank(request.getParameter("cb"))) {
            cached = false;
        }

        if (cached) {
            fetchAssetFromFile(file, response);
        } else {
            fetchAssetFromURL(urlString, request, response);
        }
    }

    /**
     * Asset storage clear.
     *
     * @param response the response
     */
    @RequestMapping(value = "/clear")
    // @RolesAllowed(AuthoritiesConstants.ADMIN) // TODO: SAC: Why doesn't this work... need to figure
    // it out!
    public final void assetStorageClear(HttpServletResponse response) {
        String headerMessage;

        try {
            File file = new File(appAssetsProxyStorageDirectory);

            FileUtils.cleanDirectory(file);

            headerMessage = "Cleared Asset Storage:";
        } catch (IOException e) {
            log.error("Unable to clear Asset Storage. (1)", e);

            headerMessage = "FAILED to Asset Storage:";
        } catch (Exception e) {
            log.error("Unable to clear Asset Storage. (2)", e);

            headerMessage = "Possibly FAILED to clear Asset Storage:";
        }

        proxyAssetCacheInfoResponse(headerMessage, response);
    }

    /**
     * Proxy asset storage info.
     *
     * @param response the response
     */
    @RequestMapping(value = "/info")
    // @RolesAllowed(AuthoritiesConstants.ADMIN) // TODO: SAC: Why doesn't this work... need to figure
    // it out!
    public final void proxyAssetStorageInfo(HttpServletResponse response) {
        proxyAssetCacheInfoResponse("Asset Storage Info:", response);
    }

    // Private Helper Methods

    private void fetchAssetFromFile(File file, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        OutputStream responseOutStream = null;

        try {
            byte[] byteBuffer = new byte[MAX_READ_SIZE];
            fileInputStream = new FileInputStream(file);

            String contentType = readFirstLineFromFile(file.getCanonicalPath() + ".content-type");

            if (StrUtils.isBlank(contentType)) {
                contentType = URLConnection.guessContentTypeFromName(file.getCanonicalPath());
            }

            int readCount = fileInputStream.read(byteBuffer);

            // Set response headers and info - must be done before setting response headers and info.

            response.setHeader("Cache-Control", "public");
            response.addHeader("Cache-Control", "max-age=86400"); // cache locally for 1 day max

            response.setContentType(contentType);
            response.setStatus(HttpStatus.OK.value());

            // Return content - must be done after setting response headers and info.

            responseOutStream = response.getOutputStream();

            while (readCount != -1) {
                responseOutStream.write(byteBuffer, 0, readCount);

                readCount = fileInputStream.read(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            // This should never happen; this method should only be called if the file is known to exist.
            log.error("ProxyAsset FileNotFoundException: {}", file.getAbsoluteFile());
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IOException e) {
            // This should never happen; this method should only be called if the file is known to exist.
            log.error("ProxyAsset IOException: {}", file.getAbsoluteFile());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close proxy file input stream.", e);
                }
            }

            if (responseOutStream != null) {
                try {
                    responseOutStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close proxy response output stream.", e);
                }
            }
        }
    }

    private void fetchAssetFromURL(String urlString, HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        FileOutputStream fileStream = null;
        OutputStream responseOutStream = null;

        try {
            URL url = new URL(urlString.replaceAll(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            inStream = connection.getInputStream();

            int contentLength = connection.getContentLength();
            byte[] byteBuffer = new byte[MAX_READ_SIZE];
            String contentType = connection.getContentType();

            if (StrUtils.isBlank(contentType)) {
                contentType = URLConnection.guessContentTypeFromName(urlString);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                fetchAssetFromURL(connection.getHeaderField("Location"), request, response);

                return;
            }

            // Point of no return - response has been modified!

            response.setContentLength(contentLength);

            // see if the file exists

            String filePath = urlString.substring(6);
            File file = new File(appAssetsProxyStorageDirectory + filePath);

            if (file.exists() && !file.isDirectory()) {
                // Delete existing cached files since we are getting new ones.

                file.delete();

                File fileContentType = new File(appAssetsProxyStorageDirectory + filePath + ".content-type");

                if (fileContentType.exists()) {
                    fileContentType.delete();
                }

                file = new File(appAssetsProxyStorageDirectory + filePath);
            } else {
                String parentPath = file.getParent();

                try {
                    FileUtils.forceMkdir(new File(file.getParent()));
                } catch (Exception e) {
                    log.error("Unable to create directory for Asset Storage - check permissions for directory {}",
                            file.getParent());
                    throw e;
                }
            }

            fileStream = new FileOutputStream(file);

            int readCount = inStream.read(byteBuffer);

            // Set response headers and info - must be done before setting response headers and info.

            response.setHeader("Cache-Control", "public");
            response.addHeader("Cache-Control", "max-age=86400"); // cache locally for 1 day max

            response.setContentType(contentType);
            response.setStatus(HttpStatus.OK.value());

            // Return content - must be done after setting response headers and info.

            responseOutStream = response.getOutputStream();

            while (readCount != -1) {
                responseOutStream.write(byteBuffer, 0, readCount);

                if (fileStream != null) {
                    fileStream.write(byteBuffer, 0, readCount);
                }

                readCount = inStream.read(byteBuffer);
            }

            writeToFile(appAssetsProxyStorageDirectory + filePath + ".content-type", contentType);
        } catch (UnknownHostException e) {
            log.warn("ProxyAsset UnknownHostException: {}", urlString);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (FileNotFoundException e) {
            log.warn("ProxyAsset FileNotFoundException: {}", urlString);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            log.warn("ProxyAsset Exception: {}", urlString);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close proxy file stream.", e);
                }
            }

            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close proxy input stream.", e);
                }
            }

            if (responseOutStream != null) {
                try {
                    responseOutStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close proxy response output stream.", e);
                }
            }
        }
    }

    private static String makeUrl(String contextPath, String url) {
        if (StrUtils.isBlank(url)) {
            return null;
        }

        String assetStoragePath = "/asset-storage/asset";
        String contextAssetStoragePath = contextPath + "/asset-storage/asset";

        url = url.trim();

        while (url.startsWith(assetStoragePath)) {
            url = url.substring(assetStoragePath.length());
        }

        while (url.startsWith(contextAssetStoragePath)) {
            url = url.substring(contextAssetStoragePath.length());
        }

        if (StrUtils.isBlank(url)) {
            return null;
        }

        if (url.charAt(0) != '/') {
            url = "/" + url;
        }

        return "http:/" + url;
    }

    private final void proxyAssetCacheInfoResponse(String headerMessage, HttpServletResponse response) {
        File file = new File(appAssetsProxyStorageDirectory);

        response.setDateHeader(HttpHeaders.EXPIRES, 0);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setContentType("text/plain; charset=utf-8");

        if (file.exists() && file.isDirectory()) {
            PrintWriter printWriter = null;

            try {
                printWriter = response.getWriter();

                long sizeOfDirectory = FileUtils.sizeOfDirectory(file);

                printWriter.append(headerMessage).append('\n').append('\n');
                printWriter.append("Asset Storage size: ").append(Long.valueOf(sizeOfDirectory).toString())
                        .append('\n');

                response.setStatus(HttpStatus.OK.value());
            } catch (IOException e) {
                log.error("Unable to read Proxy Asset cache. (1)", e);

                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

                printWriter.append("FAILED to read Proxy Cache.").append('\n').append('\n');
            } catch (Exception e) {
                log.error("Unable to read Proxy Asset cache. (2)", e);

                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

                printWriter.append("FAILED to read Proxy Asset cache");
            } finally {
                try {
                    if (printWriter != null) {
                        printWriter.append("Storage Location:   ").append(appAssetsProxyStorageDirectory).append('\n');
                        printWriter.append("Date:               ")
                                .append(LocalDateTime.now().toString("yyyy-MM-dd @ HH:mm:ss")).append('\n');
                        printWriter.append("Host Name:          ").append(SystemUtils.getHostName());

                        printWriter.close();
                    }
                } catch (Exception e) {
                    log.debug("Unable to close printWriter.", e);
                }
            }
        }
    }

    private static String readFirstLineFromFile(String filename) {
        if (StrUtils.isAnyBlank(filename)) {
            return null;
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader(filename);
            bufferedReader = new BufferedReader(fileReader);

            return bufferedReader.readLine();
        } catch (IOException e) {
            log.warn("ProxyAsset readFromFile Exception: filename: {}", filename, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    log.debug("Unable to close bufferedReader.", e);
                }
            }

            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e) {
                    log.debug("Unable to close fileReader.", e);
                }
            }
        }

        return null;
    }

    private static void writeToFile(String filename, String content) {
        if (StrUtils.isAnyBlank(filename, content)) {
            return;
        }

        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            fileWriter = new FileWriter(filename, false);
            printWriter = new PrintWriter(fileWriter);

            printWriter.append(content);
        } catch (IOException e) {
            log.warn("ProxyAsset writeToFile Exception: filename: {} - contentType: {}", filename, content, e);
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    log.debug("Unable to close printWriter.", e);
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    log.debug("Unable to close fileWriter.", e);
                }
            }
        }
    }
}
