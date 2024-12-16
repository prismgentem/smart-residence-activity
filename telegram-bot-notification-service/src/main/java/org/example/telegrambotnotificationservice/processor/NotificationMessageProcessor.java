package org.example.telegrambotnotificationservice.processor;

public interface NotificationMessageProcessor<T> {
    String processMessage(T message);

}
