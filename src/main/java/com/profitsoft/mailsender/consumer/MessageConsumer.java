package com.profitsoft.mailsender.consumer;

public interface MessageConsumer<T> {
    void consume(T message);
}
