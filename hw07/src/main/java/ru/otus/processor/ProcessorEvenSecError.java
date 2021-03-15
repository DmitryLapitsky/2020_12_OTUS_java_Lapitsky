package ru.otus.processor;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Date;

import ru.otus.model.Message;

public class ProcessorEvenSecError implements Processor {

    public int getTime(){
        return Integer.parseInt(new SimpleDateFormat("ss").format(new Date()));
    }

    @Override
    public Message process(Message message) {
        int second = getTime();
        if (second % 2 == 0) {
            throw new DateTimeException("even second");
        }
        return message;
    }

    public static void main(String[] args) {
        ProcessorEvenSecError p = new ProcessorEvenSecError();
        p.process(null);
    }
}
