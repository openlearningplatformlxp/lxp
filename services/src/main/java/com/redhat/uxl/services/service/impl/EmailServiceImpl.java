package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.EmailRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.PersonActivationToken;
import com.redhat.uxl.dataobjects.domain.PersonPasswordResetToken;
import com.redhat.uxl.services.service.AssetService;
import com.redhat.uxl.services.service.EmailService;
import com.redhat.uxl.services.service.MailService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import com.redhat.uxl.services.utils.VelocityEngineUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The type Email service.
 */
@Service
@Slf4j
@Transactional
public class EmailServiceImpl implements EmailService {
    @Inject
    private AssetService assetService;

    @Inject
    private EmailRepository emailRepository;

    @Inject
    private Environment env;

    @Inject
    private MailService mailService;

    @Inject
    private MessageSource messageSource;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private VelocityEngine velocityEngine;

    private String defaultFromEmail;
    private String defaultFromName;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        this.defaultFromEmail = env.getProperty("mail.from.email");
        this.defaultFromName = env.getProperty("mail.from.name");
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public Page<Email> findForPagedSearch(String searchOperation, String searchValue, Pageable pageable) {
        SearchBuilder<Email> searchBuilder = new SearchBuilder<>(emailRepository);

        Page<Email> emailPage = searchBuilder
                .where("or", SearchSpec.valueOf("from", searchOperation, searchValue),
                        SearchSpec.valueOf("to", searchOperation, searchValue),
                        SearchSpec.valueOf("cc", searchOperation, searchValue),
                        SearchSpec.valueOf("bcc", searchOperation, searchValue),
                        SearchSpec.valueOf("subject", searchOperation, searchValue),
                        SearchSpec.valueOf("plainText", searchOperation, searchValue),
                        SearchSpec.valueOf("htmlText", searchOperation, searchValue),
                        SearchSpec.valueOf("lastStatusMessage", searchOperation, searchValue))
                .findForPagedSearch(pageable);

        return emailPage;
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public Email getEmail(Long emailId) {
        if (emailId == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        Email email = emailRepository.findOne(emailId);

        if (email == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        return email;
    }

    @Override
    @Timed
    @Transactional(readOnly = true)
    public List<Email> getFailedEmails() {
        return emailRepository.getFailedEmails();
    }

    @Override
    @Timed
    @Transactional
    public Email resendEmail(Long emailId) {
        if (emailId == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        Email email = emailRepository.findOne(emailId);

        if (email == null) {
            throw new GeneralException(ErrorCodeGeneral.NOT_FOUND);
        }

        return sendEmail(email.getFrom(), email.getFromName(), email.getTo(), email.getCc(), email.getBcc(),
                email.getSubject(), email.getPlainText(), email.getHtmlText(), email.getPerson().getId());
    }

    @Override
    @Timed
    @Transactional
    public Email sendEmail(String to, String subject, String content, boolean isHtml, Long personId) {
        return sendEmail(null, null, to, null, null, subject, isHtml ? null : content, isHtml ? content : null,
                personId);
    }

    @Override
    @Timed
    @Transactional
    public Email sendEmail(String fromEmail, String fromName, String to, String cc, String bcc, String subject,
            String plainText, String htmlText, Long personId) {
        Email email = saveEmail(to, cc, bcc, subject, plainText, htmlText, personId);

        mailService.sendEmail(email.getId(), fromEmail, fromName, to, cc, bcc, subject, plainText, htmlText);

        return email;
    }

    @Override
    @Timed
    @Transactional
    public Email sendActivationEmail(PersonActivationToken activationToken, String baseUrl, Long personId) {
        Person person = activationToken.getPerson();
        log.debug("Sending activation email to '{}'", person.getEmail());
        Locale locale = Locale.forLanguageTag(StrUtils.firstNotBlank(person.getLangKey(), "en"));

        Map model = new HashMap();

        model.put("utils", new VelocityHelper(messageSource, locale));
        model.put("activationToken", activationToken);
        model.put("baseUrl", baseUrl);

        String subject = messageSource.getMessage("email.activation.title", null, locale);
        String content = assetService.mergeTemplateIntoString("/email/template/activationEmail.vm", CharEncoding.UTF_8,
                model);

        if (StrUtils.isBlank(content)) {
            content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/activationEmail.vm",
                    CharEncoding.UTF_8, model);
        }

        return sendEmail(person.getEmail(), subject, content, true, personId);
    }

    @Override
    @Timed
    @Transactional
    public Email sendPasswordResetMail(PersonPasswordResetToken resetToken, String baseUrl, Long personId) {
        Person person = resetToken.getPerson();
        log.debug("Sending password reset email to '{}'", person.getEmail());
        Locale locale = Locale.forLanguageTag(StrUtils.firstNotBlank(person.getLangKey(), "en"));

        Map model = new HashMap();

        model.put("utils", new VelocityHelper(messageSource, locale));
        model.put("resetToken", resetToken);
        model.put("baseUrl", baseUrl);

        String subject = messageSource.getMessage("email.reset.title", null, locale);
        String content = assetService.mergeTemplateIntoString("/email/template/passwordResetEmail.vm",
                CharEncoding.UTF_8, model);

        if (StrUtils.isBlank(content)) {
            content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/passwordResetEmail.vm",
                    CharEncoding.UTF_8, model);
        }

        return sendEmail(person.getEmail(), subject, content, true, personId);
    }

    // Public Helper Classes


    /**
     * The type Velocity helper.
     */
    @Data
    public class VelocityHelper {
        private MessageSource messageSource;
        private Locale locale;

        /**
         * Instantiates a new Velocity helper.
         *
         * @param messageSource the message source
         * @param locale        the locale
         */
        public VelocityHelper(MessageSource messageSource, Locale locale) {
            setMessageSource(messageSource);
            setLocale(locale);
        }
    }

    // Private Helper Methods

    private Email saveEmail(String to, String cc, String bcc, String subject, String plainText, String htmlText,
            Long personId) {
        try {
            Email email = new Email();
            Person person = personRepository.findOneDeletedIsFalse(personId);

            email.setTo(to);
            email.setCc(cc);
            email.setBcc(bcc);
            email.setFrom(defaultFromEmail);
            email.setFromName(StrUtils.abbreviate(defaultFromName, 32));
            email.setSubject(StrUtils.abbreviate(subject, 998));
            email.setPlainText(plainText);
            email.setHtmlText(htmlText);
            email.setPerson(person);
            email.setAttemptCount(0);

            return emailRepository.saveAndFlush(email);
        } catch (Exception e) {
            log.error(
                    "Email could not be persisted: to: {}, cc: {}, bcc: {}, subject: {}, plainText: {}, htmlText: {}, exception: {}",
                    to, cc, bcc, subject, plainText, htmlText, e.getMessage());

            throw e;
        }
    }
}
