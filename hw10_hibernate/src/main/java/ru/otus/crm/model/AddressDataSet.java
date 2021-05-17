package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "addressDataSet")
public class AddressDataSet {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL) //вариат 1 создания схемы
    @JoinColumn(name = "client_id") //вариат 1 создания схемы
    private Client client;

    public AddressDataSet() {
    }

    public AddressDataSet(String address) {
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

    protected AddressDataSet copy() {
        AddressDataSet copiedAdress = new AddressDataSet(this.address);
        copiedAdress.id = this.id;
        return copiedAdress;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street=" + address +
                '}';
    }
}
