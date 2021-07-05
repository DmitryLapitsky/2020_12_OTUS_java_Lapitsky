package ru.otus.protobuf.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CurrentValue {

    int serverValue = 0;

    private final AtomicInteger currentValue = new AtomicInteger(0);

    public int getCurrentValue(){
        return currentValue.get();
    }

    public void setServerValue(long serverValue){
        this.serverValue = (int)serverValue;
    }

    AtomicBoolean increase = new AtomicBoolean(true);

    public void action(int iterations) {
        while (currentValue.get()<=iterations) {
            try{
                currentValue.set(currentValue.get() + serverValue + 1);
                System.out.println("currentValue " + currentValue.get());
                serverValue = 0;
                Thread.sleep(1000);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
