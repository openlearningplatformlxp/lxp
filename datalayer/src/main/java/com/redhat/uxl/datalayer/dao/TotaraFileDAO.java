package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraFileDTO;

/**
 * The interface Totara file dao.
 */
public interface TotaraFileDAO {

    /**
     * Gets course file by section and name.
     *
     * @param sectionId the section id
     * @param filename  the filename
     * @return the course file by section and name
     */
    TotaraFileDTO getCourseFileBySectionAndName(Long sectionId, String filename);
}
