package ru.otus.messageImpl.back.handlers;


import ru.otus.messageImpl.dto.MsgClient;
import ru.otus.messageImpl.dto.MsgClientHandler;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientRequestToInsertHandler implements RequestHandler<MsgClient> {
    private final MsgClient msgClient;

    public ClientRequestToInsertHandler(String name, String address, String phone1, String phone2) {
        msgClient = new MsgClient(name, address, Set.of(phone1,phone2));
    }

    @Override
    public Optional<Message> handle(Message msg) {
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new MsgClientHandler(List.of(msgClient))));
    }
}
