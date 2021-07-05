package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.client.CurrentValue;
import ru.otus.protobuf.generated.FromClient;
import ru.otus.protobuf.generated.FromServer;
import ru.otus.protobuf.generated.RemoteServiceGrpc;


import java.util.concurrent.CountDownLatch;

public class GRPCClientNumbers {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8192;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("\n\n\nА теперь тоже самое, только асинхронно!!!\n\n");
        RemoteServiceGrpc.RemoteServiceStub stub = RemoteServiceGrpc.newStub(channel);

        CurrentValue clientCounter = new CurrentValue();
        Thread thread1 = new Thread(() -> clientCounter.action(50));
        thread1.start();
        final boolean[] countStart = {false};

        stub.requestNumbers(FromClient.newBuilder().setFirstValue(1).setLastValue(30).build(), new StreamObserver<FromServer>() {
            @Override
            public void onNext(FromServer value) {
                clientCounter.setServerValue(value.getResponceValue());
                System.out.println("server newValue\t"+value.getResponceValue());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Something wrong" + t);
            }

            @Override
            public void onCompleted() {
                System.out.println("request completed");
            }
        });


        latch.await();

        channel.shutdown();
    }
}
