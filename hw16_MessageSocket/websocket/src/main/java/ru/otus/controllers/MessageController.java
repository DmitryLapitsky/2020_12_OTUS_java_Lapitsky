package ru.otus.controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private final SimpMessagingTemplate template;

    public MessageController(SimpMessagingTemplate template, DBServicePhone dbServicephone, DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress,
                             MessageSystem messageSystem) {
        this.template = template;
        this.dbServicephone = dbServicephone;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.messageSystem = messageSystem;
    }

    static long i = 0;

    @MessageMapping("/message")
    @SendTo("/topic/response")
    public void getMessage(String fromServer) {
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
        fromDB();
    }

    public void messaging(RequestHandler requestHandler, long i, boolean toDb) {
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(
                MessageType.USER_DATA,
                requestHandler);
        MsClient databaseMsClient = new MsClientImpl(
                DATABASE_SERVICE_CLIENT_NAME + i,
                messageSystem,
                requestHandlerDatabaseStore,
                callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(
                MessageType.USER_DATA,
                new ClientsResponseHandler(callbackRegistry));
        MsClient frontendMsClient = new MsClientImpl(
                FRONTEND_SERVICE_CLIENT_NAME + i,
                messageSystem,
                requestHandlerFrontendStore,
                callbackRegistry);

        FrontendService frontendService = new FrontendServiceImpl(
                frontendMsClient,
                DATABASE_SERVICE_CLIENT_NAME + i);
        messageSystem.addClient(frontendMsClient);

        final List<MsgClient>[] clients = new ArrayList[]{null};
        frontendService.getAllData(data -> {
                    clients[0] = data.getData().stream().distinct().collect(Collectors.toList());
                    if (!toDb) {
                        System.out.println("sending to web from msg");
                        template.convertAndSend("/topic/response", clients[0]);
                    } else {
                        String clientName = clients[0].get(0).getName();
                        String clientAddress = clients[0].get(0).getAddress();
                        String[] clientPhones = clients[0].get(0).getPhones().toArray(new String[0]);

                        var savedClientId = dbServiceClient.saveClient(new Client(clientName, null, new HashSet<>()));

                        /// создаем phone и address
                        dbServiceAddress.saveAddress(new Address(clientAddress, savedClientId.getId()));
                        for (String el : clientPhones) {
                            dbServicephone.savePhone(new PhoneDataSet(el, savedClientId.getId()));
                        }
                        fromDB();
                    }
                }
        );
    }


    public void toDb(String name, String address, String phone1, String phone2) {
        messaging(new ClientRequestToInsertHandler(name, address, phone1, phone2), i++, true);
    }

    public void fromDB() {
        messaging(new AllClientsRequestHandler(new DBServiceImpl(dbServiceClient)), i++, false);
    }

}