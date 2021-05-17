package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionManagerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;

public class HomeWorkCache {
//    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
//    private static final String USER = "usr";
//    private static final String PASSWORD = "pwd";

    private static final String URL = "jdbc:postgresql://localhost:5432/demoDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkCache.class);

    public static void main(String[] args) throws NoSuchMethodException {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionManager = new TransactionManagerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);//done
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient);

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionManager, dataTemplateClient);

        var client = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        long begin = System.currentTimeMillis();
        dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        long cacheTime = System.currentTimeMillis() - begin;

        dbServiceClient.cleanCache();
        long begin2 = System.currentTimeMillis();
        dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));

        log.info("cache time " + cacheTime);
        log.info("no cache time " + (System.currentTimeMillis() - begin2));

        /*
          -Xms512m
          -Xmx512m
 */
        for(int i = 0; i< 1000_000; i++){
            dbServiceClient.saveClient(new Client("Client_" + i));
        }
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
