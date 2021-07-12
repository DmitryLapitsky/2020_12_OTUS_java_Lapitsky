package ru.otus.jdbc.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("address")
public class Address {

    @Id
    private Long id;

    private String address;

    private Long clientId;

    public Address(){}

    public Address(String address, Long clientId) {
        this.id = null;
        this.clientId = clientId;
        this.address = address;
    }

    @PersistenceConstructor
    public Address(Long id, String address, Long clientId) {
        this.id = id;
        this.clientId = clientId;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    @Nonnull
    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return address;
    }
}