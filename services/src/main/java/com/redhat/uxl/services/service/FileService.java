package com.redhat.uxl.services.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * The interface File service.
 */
public interface FileService {
    /**
     * Upload.
     *
     * @param userId     the user id
     * @param evidenceId the evidence id
     * @param file       the file
     * @param fileArea   the file area
     * @param component  the component
     * @throws Exception the exception
     */
    void upload(Long userId, Long evidenceId, MultipartFile file, String fileArea, String component) throws Exception;
}
