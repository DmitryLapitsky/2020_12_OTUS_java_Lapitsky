package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileSerializer implements Serializer {
    String fileName = "";

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        String jsonStr = new Gson().toJson(data);
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            Files.write(path, jsonStr.getBytes());
        }
        //формирует результирующий json и сохраняет его в файл
    }
}
