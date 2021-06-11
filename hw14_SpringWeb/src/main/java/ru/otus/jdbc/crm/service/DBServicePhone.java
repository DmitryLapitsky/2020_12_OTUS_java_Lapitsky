package ru.otus.jdbc.crm.service;

import ru.otus.jdbc.crm.model.PhoneDataSet;

import java.util.List;
import java.util.Optional;

public interface DBServicePhone {

    PhoneDataSet savePhone(PhoneDataSet phone);

    Optional<PhoneDataSet> getPhone(long id);

    List<PhoneDataSet> findAll();
}
