package ru.otus.jdbc.crm.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.jdbc.crm.model.PhoneDataSet;

import java.util.Optional;


public interface PhoneRepository extends CrudRepository<PhoneDataSet, Long> {


    Optional<PhoneDataSet> findByPhone(@Param("phone") String phone);

    @Query("select * from phone where upper(phone) = upper(:phone)")
    Optional<PhoneDataSet> findPhoneIgnoreCase(@Param("phone") String phone);

    @Modifying
    @Query("update phone set phone = :newPhone where id = :id")
    void updatePhone(@Param("id") long id, @Param("newPhone") String newPhone);
}
