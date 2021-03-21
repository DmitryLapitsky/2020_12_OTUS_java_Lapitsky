package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        Map<String, Double> map = new TreeMap<>(data.stream()
                .collect(Collectors.groupingBy(
                        Measurement::getName,
                        Collectors.summingDouble(Measurement::getValue))
                )
        );
        return map;
    }
}
