package ru.otus.messageImpl.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messageImpl.dto.MsgClientHandler;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class ClientsResponseHandler implements RequestHandler<MsgClientHandler> {//UserDataHandler
    private static final Logger logger = LoggerFactory.getLogger(ClientsResponseHandler.class);

    private final CallbackRegistry callbackRegistry;

    public ClientsResponseHandler(CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("Response message:{}", msg);
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
            } else {
                logger.error("callback for Id:{} not found", msg.getCallbackId());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
