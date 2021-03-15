package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

public class FileLoader implements Loader {
String fileName = "";

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            return null;
        }


        String read = new String(Files.readAllBytes(path));

        Gson gson = new Gson();
        Measurement[] ms = gson.fromJson(read, Measurement[].class);
        return Arrays.asList(ms);
    }
}
