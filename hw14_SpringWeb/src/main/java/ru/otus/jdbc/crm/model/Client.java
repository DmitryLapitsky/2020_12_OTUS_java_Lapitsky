package ru.otus.jdbc.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Set;

@Table("client")
public class Client {

    @Id
    @Nonnull
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Set<PhoneDataSet> phone;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    public Client(){}

    public Client(String name, Address address, Set<PhoneDataSet> phone) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    @PersistenceConstructor
    public Client(Long id, String name, Address address, Set<PhoneDataSet> phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<PhoneDataSet> getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", address=" + address +
                '}';
    }
}
