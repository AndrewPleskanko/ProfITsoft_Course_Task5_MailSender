package com.profitsoft.mailsender.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageSendingStatus;
import com.profitsoft.mailsender.repository.EmailMessageRepository;
import com.profitsoft.mailsender.services.inrerfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending emails.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${email.max.resend.attempts}")
    private int maxResendAttempts;

    private final JavaMailSender mailSender;
    private final EmailMessageRepository emailMessageRepository;

    /**
     * Sends an email message.
     *
     * @param emailMessage the email message to be sent
     */
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("Preparing to send email to: {}", emailMessage.getRecipientEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getRecipientEmail());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getContent());
        message.setFrom(from);
        try {
            mailSender.send(message);
            emailMessage.setStatus(MessageSendingStatus.SENT);
            log.debug("Email sent successfully to: {}", emailMessage.getRecipientEmail());
        } catch (MailException e) {
            log.error("Error sending email to: {}. Error message: {}",
                    emailMessage.getRecipientEmail(), e.getMessage());
            emailMessage.setStatus(MessageSendingStatus.ERROR);
        }

        emailMessage.setAttemptCount(emailMessage.getAttemptCount() + 1);
        emailMessage.setLastAttemptTime(LocalDate.now());

        log.debug("Email message status: {}", emailMessage);
        emailMessageRepository.save(emailMessage);
        log.info("Email message saved to the repository");
    }

    /**
     * Resends failed emails every 5 minutes.
     */
    @Override
    public void resendFailedEmails() {
        log.info("Starting to resend failed emails");
        List<EmailMessage> failedEmails = emailMessageRepository.findByStatus(MessageSendingStatus.ERROR);
        for (EmailMessage failedEmail : failedEmails) {
            if (failedEmail.getAttemptCount() > maxResendAttempts) {
                log.info("Skipping resend for email to: {}. Max resend attempts reached.",
                        failedEmail.getRecipientEmail());
                emailMessageRepository.delete(failedEmail);
                continue;
            }
            try {
                this.sendEmail(failedEmail);
                failedEmail.setStatus(MessageSendingStatus.SENT);
            } catch (Exception e) {
                log.error("Error resending email to: {}. Error message: {}",
                        failedEmail.getRecipientEmail(), e.getMessage());

                failedEmail.setAttemptCount(failedEmail.getAttemptCount() + 1);
                failedEmail.setLastAttemptTime(LocalDate.now());
            }
            emailMessageRepository.save(failedEmail);
        }
        log.debug("Finished resending failed emails");
    }
}
