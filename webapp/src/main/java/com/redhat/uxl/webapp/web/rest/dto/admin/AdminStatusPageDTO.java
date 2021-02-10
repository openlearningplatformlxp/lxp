package com.redhat.uxl.webapp.web.rest.dto.admin;

import com.redhat.uxl.dataobjects.domain.Email;
import java.util.List;
import lombok.Data;

/**
 * The type Admin status page dto.
 */
@Data
public class AdminStatusPageDTO {
    private long emailFailedCount;
    private long emailPendingCount;

    /**
     * Value of admin status page dto.
     *
     * @param failedEmails     the failed emails
     * @param emailMaxAttempts the email max attempts
     * @return the admin status page dto
     */
    public static AdminStatusPageDTO valueOf(List<Email> failedEmails, long emailMaxAttempts) {
        AdminStatusPageDTO dto = new AdminStatusPageDTO();
        long emailFailedCount = 0;
        long emailPendingCount = 0;

        if (failedEmails != null) {
            for (Email email : failedEmails) {
                if (email.getAttemptCount() >= emailMaxAttempts) {
                    emailFailedCount++;
                } else {
                    emailPendingCount++;
                }
            }
        }

        dto.setEmailFailedCount(emailFailedCount);
        dto.setEmailPendingCount(emailPendingCount);

        return dto;
    }
}
