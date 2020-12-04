package com.kirill.fl.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FinalStateMachineImpl implements FinalStateMachine {

    private int rank;
    private String start;
    private String finish;
    private Map<String, List<String>> inputs;
    private Map<String, Map<String, String>> matrix;
    private String currentState;

    @JsonCreator
    public FinalStateMachineImpl(@JsonProperty("start") final String start) {
        this.start = start;
        this.currentState = start;
    }

    @Override
    public AbstractMap.SimpleEntry<Boolean, Integer> find(String token, int offset) {
        int counter = 0;
        boolean isCorrect = true;
        for (int i = offset; i < token.length(); i++) {
            if (!nextState(String.valueOf(token.charAt(i)))) {
                break;
            }
            counter++;
        }

        if (!finish.equals(currentState)) {
            isCorrect = false;
        }

        return new AbstractMap.SimpleEntry<>(isCorrect, counter);
    }

    @Override
    public boolean nextState(String word) {
        Map<String, String> transitions = matrix.get(currentState);
        if (transitions != null) {
            for (Map.Entry<String, String> transition : transitions.entrySet()) {
                if (transition.getKey().equals(word)) {
                    currentState = transition.getValue();
                    return true;
                } else if (inputs != null) {
                    if (inputs.get(transition.getKey()).contains(word)) {
                        currentState = transition.getValue();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getCurrentState() {
        return currentState;
    }

    @Override
    public int getRank() {
        return rank;
    }
}
