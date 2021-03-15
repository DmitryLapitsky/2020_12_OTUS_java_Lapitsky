package ru.otus.listener;

import ru.otus.model.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerHistory implements Listener {
    static Map<Integer, List<Message>> history = new HashMap<>();
    static int count = 0;

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        history.put(count++, List.of(oldMsg, newMsg));
        System.out.println("Listener history:\n"+ history);
    }

    public Map<Integer, List<Message>> getHistory(){
        return history;
    }
}
