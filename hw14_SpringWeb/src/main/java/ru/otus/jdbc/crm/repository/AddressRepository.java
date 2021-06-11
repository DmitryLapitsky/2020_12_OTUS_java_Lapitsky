package ru.otus.jdbc.crm.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.jdbc.crm.model.Address;

import java.util.Optional;


public interface AddressRepository extends CrudRepository<Address, Long> {


    Optional<Address> findByAddress(@Param("address") String address);

    @Query("select * from phone where upper(address) = upper(:address)")
    Optional<Address> findAddressIgnoreCase(@Param("address") String address);

    @Modifying
    @Query("update address set address = :newAddress where id = :id")
    void updateAddress(@Param("id") long id, @Param("newAddress") String newAddress);
}
