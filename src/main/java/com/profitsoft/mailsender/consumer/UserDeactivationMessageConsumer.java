package com.profitsoft.mailsender.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageStatus;
import com.profitsoft.mailsender.services.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumer responsible for processing user deactivation messages from Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeactivationMessageConsumer implements MessageConsumer {

    private final EmailServiceImpl emailService;

    /**
     * Consumes user deactivation messages from Kafka and attempts to send emails based on those messages.
     *
     * @param emailMessage the email message to be sent
     */
    @Override
    @KafkaListener(topics = "user-deactivation", groupId = "group_id")
    public void consume(EmailMessage emailMessage) {
        try {
            emailService.sendEmail(emailMessage);
            emailMessage.setStatus(MessageStatus.SENT);
            log.info("Email sent: {}", emailMessage);
        } catch (Exception e) {
            emailMessage.setStatus(MessageStatus.ERROR);
            log.error("Error sending email: {}", emailMessage, e);
        }
    }
}
