package com.redhat.uxl.services.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Amazon s 3 service.
 */
public interface AmazonS3Service {
    /**
     * Make external url string.
     *
     * @param urlPart the url part
     * @return the string
     */
    String makeExternalURL(String urlPart);

    /**
     * Upload file.
     *
     * @param file     the file
     * @param filename the filename
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    void uploadFile(MultipartFile file, String filename) throws InterruptedException, IOException;
}
