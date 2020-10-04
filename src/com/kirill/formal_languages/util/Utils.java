package com.kirill.formal_languages.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    public static void writeToConsole(String string) {
        System.out.println(string);
    }

    public static void writeToFile(String string, String path) throws IOException {
        FileWriter fw = new FileWriter(new File(path));
        fw.write(string);
        fw.close();
    }
}
