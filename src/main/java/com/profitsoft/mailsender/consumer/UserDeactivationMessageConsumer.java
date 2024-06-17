package com.profitsoft.mailsender.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.services.inrerfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumer responsible for processing user deactivation messages from Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeactivationMessageConsumer implements MessageConsumer<EmailMessage> {

    private final EmailService emailService;

    /**
     * Consumes user deactivation messages from Kafka and attempts to send emails based on those messages.
     *
     * @param emailMessage the email message to be sent
     */
    @Override
    @KafkaListener(topics = "user-deactivation", groupId = "group_id")
    public void consume(EmailMessage emailMessage) {
        log.info("Received a message from Kafka: {}", emailMessage);
        emailService.sendEmail(emailMessage);
        log.debug("Email sent: {}", emailMessage);
    }
}
