package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.*;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        Map<String, Double> map = new TreeMap<>();



        for (Measurement m : data) {
            if (!map.keySet().contains(m.getName())) {
                map.put(m.getName(), m.getValue());
            } else {
                double summ = map.get(m.getName()) + m.getValue();
                map.put(m.getName(), summ);
            }
        }
        return map;
    }
}
