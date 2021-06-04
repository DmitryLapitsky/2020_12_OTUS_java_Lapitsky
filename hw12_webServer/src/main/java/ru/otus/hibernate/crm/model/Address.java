package ru.otus.hibernate.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "addressDataSet")
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL) //вариат 1 создания схемы
    @JoinColumn(name = "client_id") //вариат 1 создания схемы
    private Client client;

    public Address() {
    }

    public Address(String address) {
        this.address = address;
    }

      public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

      public void setClient(Client client) {
        this.client = client;
    }

    protected Address copy() {
        Address copiedAdress = new Address(this.address);
        copiedAdress.id = this.id;
        return copiedAdress;
    }

    @Override
    public String toString() {
        return address;
    }
}
