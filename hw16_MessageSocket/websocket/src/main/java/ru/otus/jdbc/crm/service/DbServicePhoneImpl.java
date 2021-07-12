package ru.otus.jdbc.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.jdbc.crm.model.PhoneDataSet;
import ru.otus.jdbc.crm.repository.PhoneRepository;
import ru.otus.jdbc.sessionclient.TransactionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final TransactionClient transactionClient;
    private final PhoneRepository phoneRepository;

    public DbServicePhoneImpl(TransactionClient transactionClient, PhoneRepository phoneRepository) {
        this.transactionClient = transactionClient;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public PhoneDataSet savePhone(PhoneDataSet phone) {
        return transactionClient.doInTransaction(() -> {
            var savedphone = phoneRepository.save(phone);
            log.info("saved phone: {}", savedphone);
            return savedphone;
        });
    }

    @Override
    public Optional<PhoneDataSet> getPhone(long id) {
        var phoneOptional = phoneRepository.findById(id);
        log.info("phone: {}", phoneOptional);
        return phoneOptional;
    }

    @Override
    public List<PhoneDataSet> findAll() {
        var phoneList = new ArrayList<PhoneDataSet>();
        phoneRepository.findAll().forEach(phoneList::add);
        log.info("phoneList:{}", phoneList);
        return phoneList;
    }
}
