package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.server.RemoteServiceImpl;

import java.io.IOException;

public class GRPCServerNumbers {

    public static final int SERVER_PORT = 8191;

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteServiceImpl remoteDBService = new RemoteServiceImpl();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
