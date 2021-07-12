package ru.otus.messageImpl.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messageImpl.dto.MsgClient;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.service.DBServiceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBServiceImpl implements DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private final List<MsgClient> database = new ArrayList<>();
    private final List<MsgClient> clientById = new ArrayList<>();

    public DBServiceImpl(DBServiceClient dbServiceClient) {
        List<Client> clients = dbServiceClient.findAll();
        for (Client client : clients) {
            database.add(new MsgClient(client));
        }
    }

    public DBServiceImpl(DBServiceClient dbServiceClient, long id) {
        clientById.add(new MsgClient(dbServiceClient.getClient(id).get()));
    }

    @Override
    public List<MsgClient> getAllData() {
        return database;
    }

    @Override
    public List<MsgClient> getClient(Long no) {
        return clientById;
    }
}
