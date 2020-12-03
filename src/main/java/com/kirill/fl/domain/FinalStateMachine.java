package com.kirill.fl.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FinalStateMachine {

    private int rank;
    private String start;
    private String finish;
    private Map<String, List<String>> inputs;
    private Map<String, Map<String, String>> matrix;
    private String currentState;

    @JsonCreator
    public FinalStateMachine(@JsonProperty("start") final String start) {
        this.start = start;
        this.currentState = start;
    }

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
}
