package ru.otus.handler;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import ru.otus.listener.ListenerHistory;
import ru.otus.model.Message;
import ru.otus.listener.Listener;
import ru.otus.processor.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        //given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });

        //when
        var result = complexProcessor.handle(message);

        //then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        //given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        //then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        //given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {
        });

        complexProcessor.addListener(listener);

        //when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        //then
        verify(listener, times(1)).onUpdated(message, message);
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

    @Test
    @DisplayName("Тестируем ошибку четной секунды")
    void evenSecException() throws ParseException, InterruptedException {
//        var processor1 = mock(ProcessorEvenSecError.class);
//        when(processor1.getTime()).thenReturn(2);
//        processor1.process(null);
//        assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> processor1.process(null));
//        try by mock, but didnt find, how to check throw of the DateTimeException
        while (true) {
            if (Integer.parseInt(new SimpleDateFormat("ss").format(new Date())) % 2 == 0) {
                break;
            }
        }

        DateTimeException exception = assertThrows(DateTimeException.class, () -> {
            new ProcessorEvenSecError().process(null);
        });
        assertEquals("even second", exception.getMessage());
        Thread.sleep(1000);
        assertDoesNotThrow(() -> new ProcessorEvenSecError().process(null));
    }

    @Test
    @DisplayName("Тестируем listenerHistory четной секунды")
    void testListener() {
        var message = new Message.Builder(1L).field11("fiel11").field12("field12").build();
        ListenerHistory history = new ListenerHistory();
        history.onUpdated(message, message);
        message = new ProcessorSwapFields11_12().process(message);
        Assertions.assertNotEquals(history.getHistory().get(0).get(0).toString(), message.toString());
    }

}