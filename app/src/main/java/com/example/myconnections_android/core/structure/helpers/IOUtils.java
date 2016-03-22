package com.example.myconnections_android.core.structure.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String toString(InputStream is) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStreamReader in = new InputStreamReader(is, "UTF-8");

        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            result.append(buff, 0, read);
        }
        return result.toString();
    }
}
