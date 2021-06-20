package ru.otus.hibernate.crm.model;


import javax.persistence.*;

@Entity
@Table(name = "phoneDataSet")
public class PhoneDataSet {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "phone_number")
    private String number;


    @ManyToOne(cascade = CascadeType.ALL) //вариат 1 создания схемы
    @JoinColumn(name = "client_id") //вариат 1 создания схемы
    private Client client;

    public PhoneDataSet(){

    }

    public PhoneDataSet(String number){
        this.number = number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getNumber(){
        return number;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    protected PhoneDataSet copy() {
        PhoneDataSet copiedPhone = new PhoneDataSet(this.number);
        copiedPhone.id = this.id;
        return copiedPhone;
    }

    @Override
    public String toString() {
        return number;
    }
}
