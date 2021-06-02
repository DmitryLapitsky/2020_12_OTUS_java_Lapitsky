package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl implements EntityClassMetaData {

    private Class<?> classParse;

    private Constructor<?> constructor;

    private Field fieldId;

    private Field[] fieldsAll;

    private List<Field> fieldsWithoutId = new ArrayList<Field>();

    public EntityClassMetaDataImpl(Class<?> classParse) throws NoSuchMethodException {
        this.classParse = classParse;
        fieldsAll = classParse.getDeclaredFields();
        for (Field field : classParse.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                this.fieldId = field;
            } else {
                this.fieldsWithoutId.add(field);
            }
        }
        this.constructor = classParse.getConstructor();
    }

    @Override
    public String getName() {
        return classParse.getSimpleName();
    }

    @Override
    public <T> Constructor<T> getConstructor() {
        return (Constructor<T>) constructor;
    }

    @Override
    public Field getIdField() {
        return fieldId;
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(fieldsAll);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
