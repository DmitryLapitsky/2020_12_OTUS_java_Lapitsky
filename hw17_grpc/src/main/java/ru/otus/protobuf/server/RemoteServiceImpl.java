package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.FromClient;
import ru.otus.protobuf.generated.FromServer;

public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void requestNumbers(FromClient fromClient, StreamObserver<FromServer> responseObserver) {
        if(fromClient.getLastValue() == -1){
            responseObserver.onCompleted();
        }
        long first = fromClient.getFirstValue();
        long last = fromClient.getLastValue();
        for (long i = first; i <= last; i++) {
            responseObserver.onNext(FromServer.newBuilder().setResponceValue(i).build());
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

}
