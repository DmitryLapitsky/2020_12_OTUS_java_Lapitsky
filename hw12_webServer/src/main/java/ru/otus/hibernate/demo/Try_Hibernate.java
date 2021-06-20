package ru.otus.hibernate.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.core.repository.DataTemplateHibernate;
import ru.otus.hibernate.core.repository.HibernateUtils;
import ru.otus.hibernate.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hibernate.crm.model.Address;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.PhoneDataSet;
import ru.otus.hibernate.crm.service.DbServiceClientImpl;

import java.util.List;

public class Try_Hibernate {

    ////my
    private static final String URL = "jdbc:postgresql://localhost:5432/demoDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";
    ////

    private static final Logger log = LoggerFactory.getLogger(Try_Hibernate.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        Client client = new Client("original Name");
        Address address = new Address("parallel street");
        address.setClient(client);
        client.setAddress(address);
        PhoneDataSet phone1 = new PhoneDataSet("mts");
        PhoneDataSet phone2 = new PhoneDataSet("beeline");
        phone1.setClient(client);
        phone2.setClient(client);
        client.setPhones(List.of(phone1,phone2));
        Client savedClient = dbServiceClient.saveClient(client);

        Client lastSavedClient = dbServiceClient.getClient(savedClient.getId()).orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        log.info("clientSelected:{}", lastSavedClient);

        lastSavedClient.setName("new Name");
        lastSavedClient.getPhones().get(0).setNumber("0000");

        dbServiceClient.saveClient(lastSavedClient);
        var clientUpdated = dbServiceClient.getClient(lastSavedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + lastSavedClient.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.getAll().forEach(it -> log.info("client:{}", it));

    }
}
