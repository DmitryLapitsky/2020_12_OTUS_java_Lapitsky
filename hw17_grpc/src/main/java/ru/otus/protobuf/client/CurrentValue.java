package ru.otus.protobuf.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CurrentValue {

    private final AtomicInteger currentValue = new AtomicInteger(0);

    public int getCurrentValue(){
        return currentValue.get();
    }

    public void setCurrentValue(int value){
        currentValue.set(value);
        increase.set(false);
    }

    AtomicBoolean increase = new AtomicBoolean(true);

    public synchronized void action(int iterations) {
        while (currentValue.get()<=iterations) {
            try{
                while(increase.get()) {
                    currentValue.incrementAndGet();
                    break;
                }
                System.out.println("currentValue " + currentValue.get());
                increase.set(true);
                Thread.sleep(1000);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
