package ru.otus.messageImpl.back.handlers;


import ru.otus.messageImpl.back.DBService;
import ru.otus.messageImpl.dto.MsgClientHandler;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;

import java.util.Optional;


public class ClientByIdRequestHandler implements RequestHandler<MsgClientHandler> {
    private final DBService dbService;
    private final long id;

    public ClientByIdRequestHandler(DBService dbService, long id) {
        this.dbService = dbService;
        this.id = id;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new MsgClientHandler(dbService.getClient(id))));
    }
}
