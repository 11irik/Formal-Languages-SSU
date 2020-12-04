package com.kirill.fl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.kirill.fl.domain.FinalStateMachine;
import com.kirill.fl.domain.FinalStateMachineImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FinalStateMachineTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType javaType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, FinalStateMachineImpl.class);

    @Test
    public void testMachine() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);
        keyword.nextState("b");
        keyword.nextState("e");
        keyword.nextState("g");

        Assert.assertEquals(keyword.getCurrentState(), "q3");
    }

    @Test
    public void testMachineInputs() throws IOException {

        List<FinalStateMachine> numbers = objectMapper.readValue(new File("./src/test/resources/number.json"), javaType);
        FinalStateMachine number = numbers.get(0);
        number.nextState("+");
        number.nextState("1");
        number.nextState("2");

        Assert.assertEquals(number.getCurrentState(), "q1");
    }

    @Test
    public void testFind() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        boolean found = keyword.find("begin", 0).getKey();

        Assert.assertEquals(keyword.getCurrentState(), "q5");
        Assert.assertTrue(found);
    }

    @Test
    public void testFindBad() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        boolean found = keyword.find("beg", 0).getKey();

        Assert.assertEquals(keyword.getCurrentState(), "q3");
        Assert.assertFalse(found);
    }

    @Test
    public void longerLineTest() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        boolean found = keyword.find("begin and something more", 0).getKey();

        Assert.assertTrue(found);
    }
}
