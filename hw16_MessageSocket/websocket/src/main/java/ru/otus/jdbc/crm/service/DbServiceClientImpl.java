package ru.otus.jdbc.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.repository.ClientRepository;
import ru.otus.jdbc.sessionclient.TransactionClient;

import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionClient transactionClient;

    public DbServiceClientImpl(ClientRepository clientRepository, TransactionClient transactionClient) {
        this.clientRepository = clientRepository;
        this.transactionClient = transactionClient;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionClient.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);

            log.info("savedClient client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(Long no) {
            var clientOptional = clientRepository.findById(no);
            log.info("client: {}", clientOptional);
            return clientOptional;

    }

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findAll();
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
