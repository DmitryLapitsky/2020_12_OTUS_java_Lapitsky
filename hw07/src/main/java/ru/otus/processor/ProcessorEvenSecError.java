package ru.otus.processor;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import ru.otus.model.Message;

public class ProcessorEvenSecError implements Processor {

    public int getTime(){
        return LocalDateTime.now().getSecond();
    }

    @Override
    public Message process(Message message) {
        int second = getTime();
        if (second % 2 == 0) {
            throw new DateTimeException("even second");
        }
        return message;
    }
}
