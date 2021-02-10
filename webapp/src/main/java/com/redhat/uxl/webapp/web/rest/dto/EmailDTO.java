package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * The type Email dto.
 */
@Data
public class EmailDTO {
    /**
     * The Id.
     */
    Long id;
    /**
     * The From.
     */
    String from;
    /**
     * The From name.
     */
    String fromName;
    /**
     * The Subject.
     */
    String subject;
    /**
     * The Plain text.
     */
    String plainText;
    /**
     * The Html text.
     */
    String htmlText;
    /**
     * The Sent date.
     */
    DateTime sentDate;
    /**
     * The Attempt count.
     */
    Integer attemptCount;
    /**
     * The Last attempt date.
     */
    DateTime lastAttemptDate;
    /**
     * The Last status message.
     */
    String lastStatusMessage;

    /**
     * The To.
     */
    List<String> to;
    /**
     * The Cc.
     */
    List<String> cc;
    /**
     * The Bcc.
     */
    List<String> bcc;

    /**
     * Value of email dto.
     *
     * @param email the email
     * @return the email dto
     */
    public static EmailDTO valueOf(Email email) {
        if (email == null) {
            return new EmailDTO();
        }

        EmailDTO dto = new EmailDTO();

        dto.setAttemptCount(email.getAttemptCount());
        dto.setFrom(email.getFrom());
        dto.setFromName(email.getFromName());
        dto.setHtmlText(email.getHtmlText());
        dto.setId(email.getId());
        dto.setLastAttemptDate(email.getLastAttemptDate());
        dto.setLastStatusMessage(email.getLastStatusMessage());
        dto.setPlainText(email.getPlainText());
        dto.setSentDate(email.getSentDate());
        dto.setSubject(email.getSubject());

        dto.setBcc(toList(email.getBcc()));
        dto.setCc(toList(email.getCc()));
        dto.setTo(toList(email.getTo()));

        return dto;
    }

    // Private Helper Methods

    private static List<String> toList(String value) {
        if (value == null) {
            return null;
        }

        if (StrUtils.isBlank(value)) {
            return new ArrayList<>(0);
        }

        List<String> values = new ArrayList<>();

        for (String singleValue : value.split(";")) {
            if (StrUtils.isNotBlank(singleValue)) {
                values.add(singleValue.trim());
            }
        }

        return values;
    }

    /**
     * Value of page.
     *
     * @param emailPage the email page
     * @return the page
     */
    public static Page<EmailDTO> valueOf(Page<Email> emailPage) {
        if (emailPage == null || !emailPage.hasContent()) {
            return new PageImpl<>(new ArrayList<>(0));
        }

        List<EmailDTO> dtos = new ArrayList<>(emailPage.getNumberOfElements());

        for (Email email : emailPage.getContent()) {
            dtos.add(EmailDTO.valueOf(email));
        }

        Pageable pageable = PageRequest.of(emailPage.getNumber(), emailPage.getSize(), emailPage.getSort());

        return new PageImpl<>(dtos, pageable, emailPage.getTotalElements());
    }
}
