package ru.otus.messageImpl.front;


import ru.otus.messageImpl.dto.MsgClientHandler;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;


public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }


    @Override
    public void getAllData(MessageCallback<MsgClientHandler> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new MsgClientHandler(),
                MessageType.USER_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
