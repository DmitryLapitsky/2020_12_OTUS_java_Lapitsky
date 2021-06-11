package ru.otus.jdbc.sessionclient;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {
}
