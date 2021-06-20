package ru.otus.domain;

import java.util.Set;

public class Client {

    Long id;

    String name;

    Set<PhoneDataSet> phone;

    Address address;

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    public Client(String name, Address address, Set<PhoneDataSet> phone) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

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

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhones(Set<PhoneDataSet> phone) {
        this.phone = phone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
