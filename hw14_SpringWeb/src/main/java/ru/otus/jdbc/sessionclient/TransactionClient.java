package ru.otus.jdbc.sessionclient;

public interface TransactionClient {

    <T> T doInTransaction(TransactionAction<T> action);
}
