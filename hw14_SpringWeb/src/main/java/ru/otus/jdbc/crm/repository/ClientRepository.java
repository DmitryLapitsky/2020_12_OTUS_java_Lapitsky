package ru.otus.jdbc.crm.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.jdbc.crm.model.Client;

import java.util.List;


public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAll();
}
