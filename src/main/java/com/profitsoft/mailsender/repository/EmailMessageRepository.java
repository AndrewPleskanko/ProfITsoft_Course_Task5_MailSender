package com.profitsoft.mailsender.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageStatus;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
    List<EmailMessage> findByStatus(MessageStatus status);

    List<EmailMessage> findByRecipientEmail(String recipientEmail);
}