package ru.otus.dataprocessor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProcessorTest {

    //Надо реализовать методы классов и убедиться, что тест проходит

    @Test
    @DisplayName("Из файла читается json, обрабатывается, результат сериализуется в строку")
    void processingTest(@TempDir Path tempDir) throws IOException, URISyntaxException {

        //given
        var inputDataFileName = "inputData.json";
        var outputDataFileName = "outputData.json";
        String fullOutputFilePath = String.format("%s%s%s", tempDir, File.separator, outputDataFileName);

        URL resource = getClass().getClassLoader().getResource(inputDataFileName);
        FileLoader loader = new FileLoader(new File(Objects.requireNonNull(resource).toURI()).getAbsolutePath());
        var processor = new ProcessorAggregator();
        var serializer = new FileSerializer(fullOutputFilePath);

        //when
        var loadedMeasurements = loader.load();
        var aggregatedMeasurements = processor.process(loadedMeasurements);
        serializer.serialize(aggregatedMeasurements);

        //then
        assertThat(loadedMeasurements.size()).isEqualTo(9);
        assertThat(aggregatedMeasurements.entrySet().size()).isEqualTo(3);


        var serializedOutput = new String(Files.readAllBytes(Paths.get(fullOutputFilePath)));

        //обратите внимание: важен порядок ключей
        assertThat(serializedOutput).isEqualTo("{\"val1\":3.0,\"val2\":30.0,\"val3\":33.0}");
    }
}
