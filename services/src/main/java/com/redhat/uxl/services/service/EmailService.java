package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Email service.
 */
public interface EmailService {
    /**
     * Find for paged search page.
     *
     * @param searchOperation the search operation
     * @param searchValue     the search value
     * @param pageable        the pageable
     * @return the page
     */
    Page<Email> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable);

    /**
     * Gets email.
     *
     * @param emailId the email id
     * @return the email
     */
    Email getEmail(Long emailId);

    /**
     * Gets failed emails.
     *
     * @return the failed emails
     */
    List<Email> getFailedEmails();

    /**
     * Resend email email.
     *
     * @param emailId the email id
     * @return the email
     */
    Email resendEmail(Long emailId);

    /**
     * Send activation email email.
     *
     * @param activationToken the activation token
     * @param baseUrl         the base url
     * @param personId        the person id
     * @return the email
     */
    Email sendActivationEmail(PersonActivationToken activationToken, String baseUrl, Long personId);

    /**
     * Send email email.
     *
     * @param to       the to
     * @param subject  the subject
     * @param content  the content
     * @param isHtml   the is html
     * @param personId the person id
     * @return the email
     */
    Email sendEmail(String to, String subject, String content, boolean isHtml, Long personId);

    /**
     * Send email email.
     *
     * @param fromEmail the from email
     * @param fromName  the from name
     * @param to        the to
     * @param cc        the cc
     * @param bcc       the bcc
     * @param subject   the subject
     * @param plainText the plain text
     * @param htmlText  the html text
     * @param personId  the person id
     * @return the email
     */
    Email sendEmail(String fromEmail, String fromName, String to, String cc, String bcc, String subject,
            String plainText, String htmlText, Long personId);

    /**
     * Send password reset mail email.
     *
     * @param resetToken the reset token
     * @param baseUrl    the base url
     * @param personId   the person id
     * @return the email
     */
    Email sendPasswordResetMail(PersonPasswordResetToken resetToken, String baseUrl, Long personId);
}
