package com.kirill.fl.domain;

import java.util.AbstractMap;

public interface FinalStateMachine {
    public AbstractMap.SimpleEntry<Boolean, Integer> find(String token, int offset);

    public boolean nextState(String word);

    public String getCurrentState();

    public int getRank();
}
