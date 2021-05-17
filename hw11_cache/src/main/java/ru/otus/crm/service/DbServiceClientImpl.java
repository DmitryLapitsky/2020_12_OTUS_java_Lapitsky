package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    private HwCache<Long, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        cache = new MyCache<>();
    }

    @Override
    public Client saveClient(Client client) {

        return transactionManager.doInTransaction(connection -> {
            if (client.getId() == null) {
                long clientId = 0;
                try {
                    clientId = clientDataTemplate.insert(connection, client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                log.info("caching client: {}", createdClient);
                cache.put(createdClient.getId(), createdClient);
                return createdClient;
            }
            clientDataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            log.info("updating client in cache client: {}", client);
            cache.put(client.getId(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        if(cache.get(id) != null){
            return Optional.of(cache.get(id));
        }
        return transactionManager.doInTransaction(connection -> {
            var clientOptional = clientDataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(connection -> {
            var clientList = clientDataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    public void cleanCache(){
        cache = new MyCache<>();
    }
}
