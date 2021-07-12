package ru.otus.messageImpl.back;


import ru.otus.jdbc.crm.model.Client;
import ru.otus.messageImpl.dto.MsgClient;

import java.util.List;
import java.util.Optional;

public interface DBService {
    List<MsgClient> getAllData();

    List<MsgClient> getClient(Long no);
}
