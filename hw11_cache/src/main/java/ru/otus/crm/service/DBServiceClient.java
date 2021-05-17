package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;

public interface DBServiceClient {

    Client saveClient(Client client);

    Object getClient(long id);

    List<Client> findAll();
}
