package ru.otus.dataprocessor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProcessorTest {

    //Надо реализовать методы классов и убедиться, что тест проходит

    @Test
    @DisplayName("Из файла читается json, обрабатывается, результат сериализуется в строку")
    void processingTest(@TempDir Path tempDir) throws IOException {
        System.out.println("tempDir\t" + tempDir);

        //given
        var inputDataFileName = "D:\\SelfLearn\\OTUS\\2020_12_OTUS_java_Lapitsky\\hw08_json\\src\\test\\resources\\inputData.json";// "inputData.json";
        var outputDataFileName = "outputData.json";
        String fullOutputFilePath = String.format("%s%s%s", tempDir, File.separator, outputDataFileName);
        System.out.println("fOutDir\t" + fullOutputFilePath);

        FileLoader loader = new FileLoader(inputDataFileName);
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
