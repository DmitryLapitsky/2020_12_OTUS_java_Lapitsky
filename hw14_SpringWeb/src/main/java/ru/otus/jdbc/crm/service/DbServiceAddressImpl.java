package ru.otus.jdbc.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.jdbc.crm.model.Address;
import ru.otus.jdbc.crm.repository.AddressRepository;
import ru.otus.jdbc.sessionclient.TransactionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceAddressImpl implements DBServiceAddress {
    private static final Logger log = LoggerFactory.getLogger(DbServiceAddressImpl.class);

    private final TransactionClient transactionClient;
    private final AddressRepository addressRepository;

    public DbServiceAddressImpl(TransactionClient transactionClient, AddressRepository addressRepository) {
        this.transactionClient = transactionClient;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionClient.doInTransaction(() -> {
            Address savedClient = addressRepository.save(address);

            log.info("savedAddress address: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Address> getAddress(long id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        log.info("address: {}", addressOptional);
        return addressOptional;

    }

    @Override
    public List<Address> findAll() {
        List<Address> addressList = new ArrayList<Address>();
        addressRepository.findAll().forEach(addressList::add);
        log.info("addressList:{}", addressList);
        return addressList;
    }
}
