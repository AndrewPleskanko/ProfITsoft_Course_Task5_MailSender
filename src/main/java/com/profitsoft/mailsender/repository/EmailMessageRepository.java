package com.profitsoft.mailsender.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageSendingStatus;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
    List<EmailMessage> findByStatus(MessageSendingStatus status);

    List<EmailMessage> findByRecipientEmail(String recipientEmail);
}