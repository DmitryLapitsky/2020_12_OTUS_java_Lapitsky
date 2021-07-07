package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.FromClient;
import ru.otus.protobuf.generated.FromServer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class RemoteServerLogic extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void requestNumbers(FromClient fromClient, StreamObserver<FromServer> responseObserver) {
        AtomicLong responceValue = new AtomicLong(fromClient.getFirstValue());
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = ()->{
            long value = responceValue.incrementAndGet();
            FromServer responce = FromServer.newBuilder().setResponceValue(value).build();
            responseObserver.onNext(responce);
            if(value == fromClient.getLastValue()){
                executor.shutdown();
                responseObserver.onCompleted();
                System.out.println("server numbers finished");
            }
        };
        executor.scheduleAtFixedRate(task, 0,2, TimeUnit.SECONDS);
    }
}
