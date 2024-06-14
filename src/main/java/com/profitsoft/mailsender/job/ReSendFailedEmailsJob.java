package com.profitsoft.mailsender.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.profitsoft.mailsender.services.inrerfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReSendFailedEmailsJob {

    private final EmailService emailService;

    @Scheduled(fixedRateString = "${email.resend.after.ms}")
    public void resendFailedEmails() {
        log.info("Resending failed emails");
        emailService.resendFailedEmails();
    }
}
