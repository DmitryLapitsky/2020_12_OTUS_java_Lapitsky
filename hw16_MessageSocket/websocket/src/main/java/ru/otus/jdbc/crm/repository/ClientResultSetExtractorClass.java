package ru.otus.jdbc.crm.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.jdbc.crm.model.Address;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.model.PhoneDataSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Client> clientList = new ArrayList<Client>();
        Set<PhoneDataSet> phones = new HashSet<>();
        while (rs.next()) {
            phones.add(new PhoneDataSet(rs.getLong("phone_id"), rs.getString("phone_phone"), Long.parseLong(rs.getString("phone_client_id"))));
            Address address = new Address(rs.getLong("address_id"), rs.getString("address_address"), Long.parseLong(rs.getString("address_client_id")));
            long newClientId = rs.getLong("new_client_id");
            if (newClientId != 0) {
                Client client = new Client(rs.getLong("client_id"), rs.getString("client_name"), address, phones);
                clientList.add(client);
                phones = new HashSet<>();
            }
        }
        return clientList;
    }
}
