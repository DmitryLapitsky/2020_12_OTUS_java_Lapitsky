package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.server.RemoteServerLogic;

import java.io.IOException;

public class GRPCServerNumbers {

    public static final int SERVER_PORT = 8192;

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteServerLogic remoteDBService = new RemoteServerLogic();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Received shutdown request");
            server.shutdown();
            System.out.println("Server stopped");
        }));
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
