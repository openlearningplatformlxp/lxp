package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Totara file service.
 */
public interface TotaraFileService {

    /**
     * Build plugin urls string.
     *
     * @param sectionId the section id
     * @param content   the content
     * @return the string
     */
    @Timed
    @Transactional(readOnly = true)
    String buildPluginUrls(Long sectionId, String content);
}
