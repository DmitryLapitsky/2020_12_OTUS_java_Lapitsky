//package ru.otus.jdbc.crm.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ClientHandler  {
//
//    List<ClientSimpleForMessageSys> clients;
//
//    public ClientHandler() {
//
//    }
//
//    public ClientHandler(List<Client> clients) {
//        List<ClientSimpleForMessageSys> cliMsg = new ArrayList<>();
//        for (Client client : clients) {
//            cliMsg.add(new ClientSimpleForMessageSys(client));
//            System.out.println("providing cli: " + new ClientSimpleForMessageSys(client));
//        }
//        this.clients = cliMsg;
//    }
//
//    @Override
//    public String toString() {
//        return "ClientHandler{" +
//                "clients=" + clients +
//                '}';
//    }
//}
