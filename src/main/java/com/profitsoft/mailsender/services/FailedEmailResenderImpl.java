package com.profitsoft.mailsender.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageStatus;
import com.profitsoft.mailsender.repository.EmailMessageRepository;
import com.profitsoft.mailsender.services.inrerfaces.FailedEmailResender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for resending failed emails.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FailedEmailResenderImpl implements FailedEmailResender {

    private final EmailServiceImpl emailService;
    private final EmailMessageRepository emailMessageRepository;

    /**
     * Resends failed emails every 5 minutes.
     */
    @Override
    @Scheduled(fixedRateString = "${spring.fixed.rate}")
    public void resendFailedEmails() {
        log.info("Starting to resend failed emails");
        List<EmailMessage> failedEmails = emailMessageRepository.findByStatus(MessageStatus.ERROR);
        for (EmailMessage failedEmail : failedEmails) {
            try {
                emailService.sendEmail(failedEmail);
                failedEmail.setStatus(MessageStatus.SENT);
            } catch (Exception e) {
                log.error("Error resending email to: {}. Error message: {}",
                        failedEmail.getRecipientEmail(), e.getMessage());

                failedEmail.setAttemptCount(failedEmail.getAttemptCount() + 1);
                failedEmail.setLastAttemptTime(LocalDate.now());
            }
            emailMessageRepository.save(failedEmail);
            log.info("Finished resending failed emails");
        }
    }
}