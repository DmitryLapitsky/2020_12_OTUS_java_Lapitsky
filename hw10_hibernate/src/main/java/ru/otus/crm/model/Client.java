package ru.otus.crm.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client implements Cloneable, Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = AddressDataSet.class, mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private AddressDataSet addressDataSet;

    @OneToMany(targetEntity = PhoneDataSet.class, mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhoneDataSet> phones;

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Client(Long id, String name, AddressDataSet street, List<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.addressDataSet.setAddress(street.getAddress());
    }

    @Override
    public Client clone() {
        Client clientClone = new Client(this.id, this.name);
        AddressDataSet copiedAddress = this.addressDataSet.copy();
        copiedAddress.setClient(clientClone);
        clientClone.setAddress(copiedAddress);

        List<PhoneDataSet> copiedPhones = new ArrayList<>();
        for(PhoneDataSet phone : this.phones){
            PhoneDataSet phoneLocal = phone.copy();
            phoneLocal.setClient(clientClone);
            copiedPhones.add(phoneLocal);
        }
        clientClone.setPhone(copiedPhones);
        return clientClone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressDataSet street){
        this.addressDataSet = street;
    }

    public void setPhone(List<PhoneDataSet> numbers){
        this.phones = numbers;
    }

    public List<PhoneDataSet> getPhones(){
        return phones;
    }
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adress='"+ addressDataSet +'\'' +
                ", phone='" + phones +'\'' +
                '}';
    }
}
