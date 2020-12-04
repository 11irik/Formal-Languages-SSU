package com.kirill.fl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.kirill.fl.domain.FinalStateMachine;
import com.kirill.fl.domain.FinalStateMachineImpl;
import com.kirill.fl.domain.Scanner;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScannerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType javaType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, FinalStateMachineImpl.class);

    private List<Path> machinesPaths;
    {
        try {
            machinesPaths = Files.walk(Paths.get("./src/test/resources")).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<FinalStateMachine> machines = new ArrayList<>();
    {
        machinesPaths.forEach(path -> {
            try {
                List<FinalStateMachine> group = objectMapper.readValue(new File(path.toString()), javaType);
                group.forEach(machine -> machine.setClassName(path.toFile().getName().split("\\.")[0]));
                machines.addAll(group);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void scannerTest() throws IOException {

        String lexerJava = "begin +123.;";

        Scanner scanner = new Scanner(machines);
        List<AbstractMap.SimpleEntry<String, String>> lexemes = scanner.scan(lexerJava);

        Assert.assertEquals(lexemes.get(0).getKey(), "begin");
        Assert.assertEquals(lexemes.get(0).getValue(), "keyword");

        Assert.assertEquals(lexemes.get(1).getKey(), " ");
        Assert.assertEquals(lexemes.get(1).getValue(), "whitespace");

        Assert.assertEquals(lexemes.get(2).getKey(), "+123");
        Assert.assertEquals(lexemes.get(2).getValue(), "number");

        Assert.assertEquals(lexemes.get(3).getKey(), ".");
        Assert.assertEquals(lexemes.get(3).getValue(), "spec");

        Assert.assertEquals(lexemes.get(4).getKey(), ";");
        Assert.assertEquals(lexemes.get(4).getValue(), "spec");
    }
}
