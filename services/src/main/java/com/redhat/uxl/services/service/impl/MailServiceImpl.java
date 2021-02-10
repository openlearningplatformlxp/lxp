package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.commonjava.utils.ThreadUtils;
import com.redhat.uxl.datalayer.repository.EmailRepository;
import com.redhat.uxl.dataobjects.domain.Email;
import com.redhat.uxl.services.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Mail service.
 */
@Service
@Slf4j
@Transactional
public class MailServiceImpl implements MailService {

    /**
     * The Email repository.
     */
    @Inject
  EmailRepository emailRepository;

  @Inject
  private Environment env;

  @Inject
  private JavaMailSender javaMailSender;
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

  @Async
  @Override
  @Timed
  @Transactional
  public void sendEmail(Long emailId, String fromEmail, String fromName, String to, String cc,
      String bcc, String subject, String plainText, String htmlText) {
    try {
      ThreadUtils.attemptToSleep(1000); // This is a hack to prevent a race condition!

      Email email = emailRepository.findOne(emailId);

      try {
        sendEmailHelper(StrUtils.isNotBlank(fromEmail) ? fromEmail : defaultFromEmail,
            StrUtils.isNotBlank(fromName) ? fromName : defaultFromName, to, cc, bcc, subject,
            plainText, htmlText);

        email.setLastStatusMessage(null);
        email.setLastAttemptDate(null);
        email.setSentDate(DateTime.now());
      } catch (Exception e) {
        email.setLastStatusMessage(e.getMessage());
        email.setLastAttemptDate(DateTime.now());
      }

      email.setAttemptCount(email.getAttemptCount() + 1);

      emailRepository.saveAndFlush(email);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  @Override
  @Timed
  @Transactional
  public boolean sendEmail(Long emailId) {
    boolean sent = false;
    Email email = emailRepository.findOne(emailId);

    try {
      sendEmailHelper(email.getFrom(), email.getFromName(), email.getTo(), email.getCc(),
          email.getBcc(), email.getSubject(), email.getPlainText(), email.getHtmlText());

      email.setLastStatusMessage(null);
      email.setLastAttemptDate(null);
      email.setSentDate(DateTime.now());

      sent = true;
    } catch (Exception e) {
      email.setLastStatusMessage(e.getMessage());
      email.setLastAttemptDate(DateTime.now());
    }

    email.setAttemptCount(email.getAttemptCount() + 1);

    emailRepository.saveAndFlush(email);

    return sent;
  }

  // Private Helper Methods

  private void sendEmailHelper(String fromEmail, String fromName, String to, String cc, String bcc,
      String subject, String plainText, String htmlText) throws Exception {
    log.debug("Send email from {} to {} with subject {}", fromEmail, to, subject);

    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);
      InternetAddress fromInternetAddress = new InternetAddress();

      fromInternetAddress.setAddress(fromEmail);

      if (StrUtils.isNotBlank(fromName)) {
        fromInternetAddress.setPersonal(fromName);
      }

      message.setFrom(fromInternetAddress);

      message.setTo(toList(to));

      if (StrUtils.isNotBlank(cc)) {
        message.setCc(toList(cc));
      }

      if (StrUtils.isNotBlank(bcc)) {
        message.setBcc(toList(bcc));
      }

      message.setSubject(subject);

      if (StrUtils.isNotBlank(plainText) && StrUtils.isNotBlank(htmlText)) {
        message.setText(plainText, htmlText);
      } else if (StrUtils.isNotBlank(plainText)) {
        message.setText(plainText, false);
      } else if (StrUtils.isNotBlank(htmlText)) {
        message.setText(htmlText, true);
      }

      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      log.error("Email could not be sent to person '{}', exception is: {}", to, e.getMessage());

      throw (e);
    }
  }

  private static String[] toList(String value) {
    if (value == null) {
      return null;
    }

    if (StrUtils.isBlank(value)) {
      return new String[0];
    }

    List<String> values = new ArrayList<>();

    for (String singleValue : value.split(";")) {
      if (StrUtils.isNotBlank(singleValue)) {
        values.add(singleValue.trim());
      }
    }

    return values.toArray(new String[0]);
  }
}
