package com.redhat.uxl.services.service;

/**
 * The interface Mail service.
 */
public interface MailService {
    /**
     * Send email.
     *
     * @param emailId   the email id
     * @param fromEmail the from email
     * @param fromName  the from name
     * @param to        the to
     * @param cc        the cc
     * @param bcc       the bcc
     * @param subject   the subject
     * @param plainText the plain text
     * @param htmlText  the html text
     */
    void sendEmail(Long emailId, String fromEmail, String fromName, String to, String cc, String bcc, String subject,
            String plainText, String htmlText);

    /**
     * Send email boolean.
     *
     * @param emailId the email id
     * @return the boolean
     */
    boolean sendEmail(Long emailId);
}
