package com.kirill.fl.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class Scanner {

    private List<FinalStateMachine> machines;
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    public void scan(String str) {
        int offset = 0;

        String resultLexeme = "";
        int lexemeOffset = 0;
        int lexemeRank = (int) POSITIVE_INFINITY;

        while (offset < str.length()) {
            for (FinalStateMachine machine : machines) {
                Map.Entry<Boolean, Integer> result = machine.find(str, offset);
                if (result.getKey()) {
                    String lexeme = str.substring(offset, offset + result.getValue());
                    if (resultLexeme.length() < lexeme.length()) {
                        resultLexeme = lexeme;
                        lexemeRank = machine.getRank();
                        lexemeOffset = result.getValue();
                    } else if (resultLexeme.length() == lexeme.length() && lexemeRank > machine.getRank()) {
                        resultLexeme = lexeme;
                        lexemeRank = machine.getRank();
                        lexemeOffset = result.getValue();
                    }
                }
            }
            System.out.println(resultLexeme);
            offset += lexemeOffset;
            resultLexeme = "";
            lexemeRank = (int) POSITIVE_INFINITY;
        }
    }
}
