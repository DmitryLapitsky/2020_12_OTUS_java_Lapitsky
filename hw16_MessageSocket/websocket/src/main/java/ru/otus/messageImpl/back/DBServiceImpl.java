package ru.otus.messageImpl.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messageImpl.dto.MsgClient;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.service.DBServiceClient;

import java.util.ArrayList;
import java.util.List;

public class DBServiceImpl implements DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private final List<MsgClient> database = new ArrayList<>();

    public DBServiceImpl(DBServiceClient dbServiceClient) {
        List<Client> clients = dbServiceClient.findAll();
        for (Client client : clients) {
            database.add(new MsgClient(client));
        }
    }

    @Override
    public List<MsgClient> getAllData() {
        return database;
    }
}
