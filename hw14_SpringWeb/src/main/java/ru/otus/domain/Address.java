package ru.otus.domain;

public class Address {

    private Long id;

    private String address;

    public Address() {
    }

    public Address(String address) {
        this.id = null;
        this.address = address;
    }

    public Address(Long id, String address) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }

}
