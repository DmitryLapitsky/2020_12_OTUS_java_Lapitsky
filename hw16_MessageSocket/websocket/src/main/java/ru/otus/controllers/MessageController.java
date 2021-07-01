package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.otus.jdbc.crm.model.Address;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.model.PhoneDataSet;
import ru.otus.messageImpl.back.DBServiceImpl;
import ru.otus.messageImpl.back.handlers.ClientRequestToInsertHandler;
import ru.otus.messageImpl.dto.MsgClient;
import ru.otus.messageImpl.back.handlers.AllClientsRequestHandler;
import ru.otus.messageImpl.front.FrontendServiceImpl;
import ru.otus.messageImpl.front.FrontendService;
import ru.otus.messageImpl.front.handlers.ClientsResponseHandler;
import ru.otus.jdbc.crm.service.DBServiceAddress;
import ru.otus.jdbc.crm.service.DBServiceClient;
import ru.otus.jdbc.crm.service.DBServicePhone;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.*;
import ru.otus.messagesystem.message.MessageType;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final DBServicePhone dbServicephone;
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;

    final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public MessageController(DBServicePhone dbServicephone, DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress) {
        this.dbServicephone = dbServicephone;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
    }

    @MessageMapping("/message")
    @SendTo("/topic/response")
    public List<MsgClient> getMessage(String[] fromServer) {
        if (fromServer.length < 4) {
            return fromDB();
        }
        String name = fromServer[0];
        String add = fromServer[1];
        String phone1 = fromServer[2];
        String phone2 = fromServer[3];
        toDb(name, add, phone1, phone2);
        return fromDB();
    }

    public List<MsgClient> messaging(RequestHandler requestHandler) {
        MessageSystem messageSystem = new MessageSystemImpl();
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(
                MessageType.USER_DATA,
                requestHandler);
        MsClient databaseMsClient = new MsClientImpl(
                DATABASE_SERVICE_CLIENT_NAME,
                messageSystem,
                requestHandlerDatabaseStore,
                callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(
                MessageType.USER_DATA,
                new ClientsResponseHandler(callbackRegistry));
        MsClient frontendMsClient = new MsClientImpl(
                FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem,
                requestHandlerFrontendStore,
                callbackRegistry);

        FrontendService frontendService = new FrontendServiceImpl(
                frontendMsClient,
                DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);

        final List<MsgClient>[] evenNumbers = new ArrayList[]{new ArrayList<>()};
        frontendService.getAllData(data -> evenNumbers[0] = data.getData().stream().collect(Collectors.toList()));
        try {
            Thread.sleep(100);
            messageSystem.dispose();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return evenNumbers[0];
    }

    public void toDb(String name, String address, String phone1, String phone2) {
        List<MsgClient> clients = messaging(new ClientRequestToInsertHandler(name, address, phone1, phone2));

        String clientName = clients.get(0).getName();
        String clientAddress = clients.get(0).getAddress();
        String[] clientPhones = clients.get(0).getPhones().toArray(new String[0]);

        var savedClientId = dbServiceClient.saveClient(new Client(clientName, null, new HashSet<>()));

        /// создаем phone и address
        dbServiceAddress.saveAddress(new Address(clientAddress, savedClientId.getId()));
        for (String el : clientPhones) {
            dbServicephone.savePhone(new PhoneDataSet(el, savedClientId.getId()));
        }
    }

    public List<MsgClient> fromDB() {
        return messaging(new AllClientsRequestHandler(new DBServiceImpl(dbServiceClient)));
    }

}
