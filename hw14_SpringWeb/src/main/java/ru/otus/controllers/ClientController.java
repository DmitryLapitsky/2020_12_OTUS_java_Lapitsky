package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.jdbc.crm.model.Address;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.model.PhoneDataSet;
import ru.otus.jdbc.crm.repository.AddressRepository;
import ru.otus.jdbc.crm.repository.ClientRepository;
import ru.otus.jdbc.crm.repository.PhoneRepository;
import ru.otus.jdbc.crm.service.DBServiceAddress;
import ru.otus.jdbc.crm.service.DBServiceClient;
import ru.otus.jdbc.crm.service.DBServicePhone;
import java.util.*;

@Controller
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final String applicationYmlMessage;
    private final PhoneRepository phoneRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final DBServicePhone dbServicephone;
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;

    public ClientController(@Value("${app.client-list-page.msg}") String applicationYmlMessage,
                            PhoneRepository phoneRepository, ClientRepository clientRepository, AddressRepository addressRepository,
                            DBServicePhone dbServicephone, DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress) {
        this.applicationYmlMessage = applicationYmlMessage;
        this.clientRepository = clientRepository;
        this.phoneRepository = phoneRepository;
        this.addressRepository = addressRepository;
        this.dbServicephone = dbServicephone;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
    }

    @GetMapping({"/"})
    public RedirectView noPath() {
        return new RedirectView("/clients", true);
    }

    @GetMapping({"/clients"})
    public String clientsListView(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("applicationYmlMessage", applicationYmlMessage);
        return "clients";
    }

    @GetMapping("/api/v1/addclient")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("address", new Address());
        model.addAttribute("phone", new PhoneDataSet());
        return "clientAdd";
    }

    @PostMapping("/clients/saveClient")
    public RedirectView clientSave(@ModelAttribute ru.otus.domain.Client client, @ModelAttribute ru.otus.domain.Address address, @ModelAttribute ru.otus.domain.PhoneDataSet phone) {

        var savedClientId = dbServiceClient.saveClient(new Client(client.getName(), null, new HashSet<>()));

        /// создаем phone и address
        dbServiceAddress.saveAddress(new Address(address.getAddress(), savedClientId.getId()));
        Set<PhoneDataSet> phones = new HashSet<>(phone.getPhone().split(",").length);
        for(String el : phone.getPhone().split(",")){
            dbServicephone.savePhone(new PhoneDataSet(el, savedClientId.getId()));
        }
        log.info("All phones");
        dbServicephone.findAll().forEach(phoneDataSet -> log.info("phone:{}", phoneDataSet));

        log.info("All clients");
        dbServiceClient.findAll().forEach(clientDB -> log.info("client:{}", clientDB));

        return new RedirectView("/clients", true);
    }

}
