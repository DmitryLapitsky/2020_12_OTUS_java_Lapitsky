package ru.otus.messageImpl.dto;

import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.model.PhoneDataSet;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.HashSet;
import java.util.Set;

public class MsgClient extends ResultDataType {
    private final String name;

    private Set<String> phones;

    private String address;

    public MsgClient(String name, String address, Set<String> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public MsgClient(Client client) {
        this.name = client.getName();
        this.address = client.getAddress().getAddress();
        Set<String> phs = new HashSet<>();
        for(PhoneDataSet ph : client.getPhone()){
            phs.add(ph.getPhone());
        }
        this.phones = phs;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPhones(){
        return phones;
    }

    public String getAddress(){
        return address;
    }

    @Override
    public String toString() {
        return "UserData{" +
                ", data='" + name + '\'' +
                ", phones=" + phones +
                ", address='" + address + '\'' +
                '}';
    }
}
