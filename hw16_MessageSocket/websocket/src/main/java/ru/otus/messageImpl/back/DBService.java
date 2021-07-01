package ru.otus.messageImpl.back;


import ru.otus.messageImpl.dto.MsgClient;

import java.util.List;

public interface DBService {
    List<MsgClient> getAllData();
}
