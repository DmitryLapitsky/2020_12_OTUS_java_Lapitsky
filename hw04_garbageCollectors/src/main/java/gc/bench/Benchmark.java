package gc.bench;

import java.util.ArrayList;
import java.util.List;

class Benchmark implements gc.bench.BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size: " + size);
        this.size = size;
    }

    private int id = 0;

    public int getId() {
        return id;
    }

    void getOOM() throws InterruptedException {
        for (int idx = 0; idx < loopCounter; idx++) {
            id = idx;
            List<String> integers = new ArrayList<>();
            for (int i = 0; i < size * idx; i++) {//17000
                integers.add(new String(new char[0]));
                if (i % 2 == 0) {
                    integers.set(i, null);
                }
            }
            Thread.sleep(10);
        }
    }
}
