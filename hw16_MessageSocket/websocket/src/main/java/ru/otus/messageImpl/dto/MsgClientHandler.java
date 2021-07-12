package ru.otus.messageImpl.dto;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

public class MsgClientHandler extends ResultDataType {

    List<MsgClient> data;

    public MsgClientHandler(){

    }

    public MsgClientHandler(List<MsgClient> data){
        this.data = data;
    }

    public List<MsgClient> getData(){
        return data;
    }

    @Override
    public String toString() {
        return "UserDataHandler_1{" +
                "data=" + data +
                '}';
    }
}
