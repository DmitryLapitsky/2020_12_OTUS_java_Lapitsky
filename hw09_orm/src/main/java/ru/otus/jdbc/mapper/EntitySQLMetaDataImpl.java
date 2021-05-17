package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private EntityClassMetaData entity;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        entity = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        System.out.println("all+++\t" + "SELECT * FROM " + entity.getName());
        return "SELECT * FROM " + entity.getName();
    }

    @Override
    public String getSelectByIdSql() {
        if (entity.getFieldsWithoutId().size() == 0) {
            throw new RuntimeException("there are any non-id fields in class");
        }
        StringBuilder elemetsNcommas = new StringBuilder();
        for (Field field : entity.getAllFields()) {
            elemetsNcommas.append(field.getName()).append(",");
        }
        elemetsNcommas.deleteCharAt(elemetsNcommas.length() - 1);
        System.out.println("sel+++\t" + "select " + elemetsNcommas + " from " + entity.getName() + " where " + entity.getIdField().getName() + "  = ?");
        return "select " + elemetsNcommas + " from " + entity.getName() + " where " + entity.getIdField().getName() + "  = ?";
    }

    @Override
    public String getInsertSql() {
        if (entity.getFieldsWithoutId().size() == 0) {
            throw new RuntimeException("there are any non-id fields in class");
        }
        StringBuilder elemetsNcommas = new StringBuilder();
        StringBuilder valuesNcommas = new StringBuilder();
        for (Field field : entity.getFieldsWithoutId()) {
            elemetsNcommas.append(field.getName()).append(",");
            valuesNcommas.append("?").append(",");
        }
        elemetsNcommas.deleteCharAt(elemetsNcommas.length() - 1);
        valuesNcommas.deleteCharAt(valuesNcommas.length() - 1);
        System.out.println("ins+++\t" + "insert into " + entity.getName() + "(" + elemetsNcommas + ") values (" + valuesNcommas + ")");
        return "insert into " + entity.getName() + "(" + elemetsNcommas + ") values (" + valuesNcommas + ")";
    }

    @Override
    public String getUpdateSql() {
        if (entity.getFieldsWithoutId().size() == 0) {
            throw new RuntimeException("there are any non-id fields in class");
        }
        StringBuilder mysteryElemetsNcommas = new StringBuilder();
        for (Field field : entity.getFieldsWithoutId()) {
            mysteryElemetsNcommas.append(field.getName()).append(" = ?").append(",");
        }
        mysteryElemetsNcommas.deleteCharAt(mysteryElemetsNcommas.length() - 1);
        System.out.println("upd+++\t" + "update " + entity.getName() + " set " + mysteryElemetsNcommas + " where " + entity.getIdField() + " = ?");
        return "update " + entity.getName() + " set " + mysteryElemetsNcommas + " where " + entity.getIdField() + " = ?";
    }
}
