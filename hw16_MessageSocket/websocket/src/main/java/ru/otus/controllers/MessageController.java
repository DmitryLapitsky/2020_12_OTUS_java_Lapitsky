package ru.otus.controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final DBServicePhone dbServicephone;
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final MessageSystem messageSystem;
    private final MessageSystem messageSystem2;

    final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public MessageController(DBServicePhone dbServicephone, DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress,
                             MessageSystem messageSystem, MessageSystem messageSystem2) {
        this.dbServicephone = dbServicephone;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.messageSystem = messageSystem;
        this.messageSystem2 = messageSystem2;
    }

    static long i = 0;

    @MessageMapping("/message")
    @SendTo("/topic/response")
    public List<MsgClient> getMessage(String fromServer) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(fromServer);
            String name = (String) obj.get("name");
            String address = (String) obj.get("address");
            String phone1 = (String) obj.get("phone1");
            String phone2 = (String) obj.get("phone2");
            toDb(name, address, phone1, phone2);
        } catch (Exception e) {
            logger.info("not enough data provided {}", fromServer);
        }

        return fromDB();
    }

    public List<MsgClient> messaging(MessageSystem messageSystem, RequestHandler requestHandler, long i) {
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(
                MessageType.USER_DATA,
                requestHandler);
        MsClient databaseMsClient = new MsClientImpl(
                DATABASE_SERVICE_CLIENT_NAME + i,
                this.messageSystem,
                requestHandlerDatabaseStore,
                callbackRegistry);
        this.messageSystem.addClient(databaseMsClient);

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(
                MessageType.USER_DATA,
                new ClientsResponseHandler(callbackRegistry));
        MsClient frontendMsClient = new MsClientImpl(
                FRONTEND_SERVICE_CLIENT_NAME + i,
                this.messageSystem,
                requestHandlerFrontendStore,
                callbackRegistry);

        FrontendService frontendService = new FrontendServiceImpl(
                frontendMsClient,
                DATABASE_SERVICE_CLIENT_NAME + i);
        this.messageSystem.addClient(frontendMsClient);

        final List<MsgClient>[] clients = new ArrayList[]{null};
        frontendService.getAllData(data -> clients[0] = data.getData().stream().distinct().collect(Collectors.toList()));
        while(clients[0]==null) {
            System.out.println("waiting");
        }
        return clients[0];
    }



    public void toDb(String name, String address, String phone1, String phone2) {
        List<MsgClient> clients =messaging(messageSystem, new ClientRequestToInsertHandler(name, address, phone1, phone2), i++);

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
        return messaging(messageSystem, new AllClientsRequestHandler(new DBServiceImpl(dbServiceClient)), i++);
    }

}