package ru.otus.messageImpl.front;


import ru.otus.messageImpl.dto.MsgClientHandler;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {
    void getAllData(MessageCallback<MsgClientHandler> dataConsumer);
}

