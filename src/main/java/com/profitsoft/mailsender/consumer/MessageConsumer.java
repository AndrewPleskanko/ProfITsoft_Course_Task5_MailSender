package com.profitsoft.mailsender.consumer;

import com.profitsoft.mailsender.entity.EmailMessage;

public interface MessageConsumer {
    void consume(EmailMessage emailMessage);
}
