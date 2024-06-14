package com.profitsoft.mailsender.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageStatus;
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
            emailMessage.setStatus(MessageStatus.SENT);
            log.info("Email sent successfully to: {}", emailMessage.getRecipientEmail());
        } catch (MailException e) {
            log.error("Error sending email to: {}. Error message: {}",
                    emailMessage.getRecipientEmail(), e.getMessage());
            emailMessage.setStatus(MessageStatus.ERROR);
        }

        emailMessage.setAttemptCount(emailMessage.getAttemptCount() + 1);
        emailMessage.setLastAttemptTime(LocalDate.now());

        log.info("Email message status: {}", emailMessage);
        emailMessageRepository.save(emailMessage);
        log.info("Email message saved to the repository");
    }
}
