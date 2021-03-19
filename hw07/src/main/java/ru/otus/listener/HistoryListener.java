package ru.otus.listener;

import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    static Map<Integer, Message> history = new HashMap<>();
    static int count = 0;

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        Message oldMsgH = new Message.Builder(oldMsg.getId()).copyBody(oldMsg).build();
        history.put(count++, oldMsgH);
    }

    public Optional<Message> findMessageById(long id) {
        for (Map.Entry<Integer, Message> histEl : history.entrySet()) {
            if (histEl.getValue().getId() == id) {
                return Optional.of(histEl.getValue());
            }
        }
        throw new UnsupportedOperationException();
    }

}
