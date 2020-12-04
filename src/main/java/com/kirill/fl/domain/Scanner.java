package com.kirill.fl.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class Scanner {

    private List<FinalStateMachine> machines;
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    public List<AbstractMap.SimpleEntry<String, String>> scan(String str) {
        List<AbstractMap.SimpleEntry<String, String>> lexemes = new ArrayList<>();
        int offset = 0;

        String resultLexeme = "";
        int lexemeOffset = 0;
        int lexemeRank = (int) POSITIVE_INFINITY;
        String lexemeClass = "";

        while (offset < str.length()) {
            for (FinalStateMachine machine : machines) {
                Map.Entry<Boolean, Integer> result = machine.find(str, offset);
                if (result.getKey()) {
                    String lexeme = str.substring(offset, offset + result.getValue());
                    if (resultLexeme.length() < lexeme.length()) {
                        resultLexeme = lexeme;
                        lexemeRank = machine.getRank();
                        lexemeOffset = result.getValue();
                        lexemeClass = machine.getClassName();
                    } else if (resultLexeme.length() == lexeme.length() && lexemeRank > machine.getRank()) {
                        resultLexeme = lexeme;
                        lexemeRank = machine.getRank();
                        lexemeOffset = result.getValue();
                        lexemeClass = machine.getClassName();
                    }
                }
            }
            lexemes.add(new AbstractMap.SimpleEntry<>(resultLexeme, lexemeClass));
            offset += lexemeOffset;
            resultLexeme = "";
            lexemeRank = (int) POSITIVE_INFINITY;
        }

        return lexemes;
    }
}
