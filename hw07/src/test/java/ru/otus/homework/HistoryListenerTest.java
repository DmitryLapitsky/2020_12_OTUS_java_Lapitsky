package ru.otus.handler.homework;


import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import ru.otus.listener.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryListenerTest {

    @Test
    void ListenerTest() {
        //given

        var historyListener = new HistoryListener();

        var id = 1L;
        var data = "33";
        var field13 = new ObjectForMessage();
        field13.setData(List.of(data));

        var message = new Message.Builder(id)
                .field10("field10")
//TODO: раскоментировать
                .field13(field13)
                .build();
        //when
        historyListener.onUpdated(message, message);
//TODO: раскоментировать
        message.getField13().setData(new ArrayList<>()); //меняем исходное сообщение

        //then
        var messageFromHistory = historyListener.findMessageById(id);
        AssertionsForClassTypes.assertThat(messageFromHistory).isPresent();
//TODO: раскоментировать
        Assertions.assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
    }
}