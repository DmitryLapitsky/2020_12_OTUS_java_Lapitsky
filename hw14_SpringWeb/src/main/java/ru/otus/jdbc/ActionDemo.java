//package ru.otus.jdbc;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import ru.otus.jdbc.crm.model.Address;
//import ru.otus.jdbc.crm.model.Client;
//import ru.otus.jdbc.crm.model.PhoneDataSet;
//import ru.otus.jdbc.crm.repository.ClientRepository;
//import ru.otus.jdbc.crm.repository.PhoneRepository;
//import ru.otus.jdbc.crm.service.DBServiceAddress;
//import ru.otus.jdbc.crm.service.DBServiceClient;
//import ru.otus.jdbc.crm.service.DBServicePhone;
//
//import java.util.HashSet;
//
//
//@Component("actionDemo")
//public class ActionDemo {
//    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);
//
//    private final PhoneRepository phoneRepository;
//    private final ClientRepository clientRepository;
//    private final DBServicePhone dbServicephone;
//    private final DBServiceClient dbServiceClient;
//    private final DBServiceAddress dbServiceAddress;
//
//    public ActionDemo(PhoneRepository phoneRepository, ClientRepository clientRepository,
//                      DBServicePhone dbServicephone, DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress) {
//        this.clientRepository = clientRepository;
//        this.phoneRepository = phoneRepository;
//        this.dbServicephone = dbServicephone;
//        this.dbServiceClient = dbServiceClient;
//        this.dbServiceAddress = dbServiceAddress;
//    }
//
//    void action() {
//
////// создаем клиента
//
//        var client = dbServiceClient.saveClient(new Client("Client", null, new HashSet<>()));
//        var savedClient = dbServiceClient.getClient(client.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
//        log.info("ClientSelected:{}", savedClient);
//
////// обновляем Client
//        dbServiceClient.saveClient(new Client(savedClient.getId(), "dbServiceSecondUpdated", null,new HashSet<>()));
//        var managerUpdated = dbServiceClient.getClient(savedClient.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
//        log.info("ClientUpdated:{}", managerUpdated);
//
///// создаем phone и address
//        dbServiceAddress.saveAddress(new Address("parallel",client.getId()));
//        var firstphone = dbServicephone.savePhone(new PhoneDataSet("dbServiceFirst" + System.currentTimeMillis(), client.getId()));
//
//        var phoneSecond = dbServicephone.savePhone(new PhoneDataSet("dbServiceSecond" + System.currentTimeMillis(), client.getId()));
//        var phoneSecondSelected = dbServicephone.getPhone(phoneSecond.getId())
//                .orElseThrow(() -> new RuntimeException("phone not found, id:" + phoneSecond.getId()));
//        log.info("phoneSecondSelected:{}", phoneSecondSelected);
//
///// обновляем phone
//        dbServicephone.savePhone(new PhoneDataSet(phoneSecondSelected.getId(), "dbServiceSecondUpdated", client.getId()));
//        var clientUpdated = dbServicephone.getPhone(phoneSecondSelected.getId())
//                .orElseThrow(() -> new RuntimeException("phone not found, id:" + phoneSecondSelected.getId()));
//        log.info("phoneUpdated:{}", clientUpdated);
//
//
///// получаем все сущности
//        log.info("All phones");
//        dbServicephone.findAll().forEach(phone -> log.info("phone:{}", phone));
//
//        log.info("All managers");
//        dbServiceClient.findAll().forEach(clientLocal -> log.info("client:{}", clientLocal));
//
///// применяем переопределенные методы репозитариев
//        var clientFoundByName = phoneRepository.findByPhone(firstphone.getPhone())
//                .orElseThrow(() -> new RuntimeException("phone not found, name:" + firstphone.getPhone()));
//        log.info("phoneFoundByName:{}", clientFoundByName);
//
//        var clientFoundByNameIgnoreCase = phoneRepository.findPhoneIgnoreCase(firstphone.getPhone().toLowerCase())
//                .orElseThrow(() -> new RuntimeException("phone not found, name:" + firstphone.getPhone()));
//        log.info("phoneFoundByNameIgnoreCase:{}", clientFoundByNameIgnoreCase);
//
//        phoneRepository.updatePhone(firstphone.getId(), "newPhone");
//        var updatedClient = phoneRepository.findById(firstphone.getId())
//                .orElseThrow(() -> new RuntimeException("phone not found"));
//
//        log.info("updatedClient:{}", updatedClient);
//
///// проверяем проблему N+1
//        log.info("checking N+1 problem");
//        var clientN = clientRepository.findById(client.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, name:" + client.getId()));
//        log.info("clientN:{}", clientN);
//
//        /*
//        получаем основную сущность:
//        [SELECT "client"."id" AS "id", "client"."label" AS "label" FROM "client" WHERE "client"."id" = ?]
//        получаем дочерние:
//        [SELECT "phone"."id" AS "id", "phone"."name" AS "name", "phone"."manager_id" AS "manager_id" FROM "phone" WHERE "phone"."manager_id" = ?]
//        */
//        log.info("select all");
//        var allClients = clientRepository.findAll();
//        log.info("allClients:{}", allClients);
//    }
//}
