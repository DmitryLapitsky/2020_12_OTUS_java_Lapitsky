package ru.otus.listener;

import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    static List<Message> history = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        history.add(oldMsg.clone());
    }

    public Optional<Message> findMessageById(long id) {
        return history.stream().filter(el-> el.getId() == id).findAny();
    }

}
