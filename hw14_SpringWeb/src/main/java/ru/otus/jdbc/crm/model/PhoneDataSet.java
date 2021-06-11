package ru.otus.jdbc.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;


@Table("phone")
public class PhoneDataSet {

    @Id
    private Long id;

    @Nonnull
    private String phone;

    @Nonnull
    private Long clientId;

    public PhoneDataSet(){}

    public PhoneDataSet(String phone, Long clientId) {
        this.id = null;
        this.clientId = clientId;
        this.phone = phone;
    }

    @PersistenceConstructor
    public PhoneDataSet(Long id, String phone, Long clientId) {
        this.id = id;
        this.phone = phone;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    @Nonnull
    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return phone;
    }
}
