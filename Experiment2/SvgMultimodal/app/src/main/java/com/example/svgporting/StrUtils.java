package com.example.svgporting;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class StrUtils {
    /*
    * "id_elements": [
        "#insieme-dominio"
    ],
    "style_names": [
        "fill"
    ],
    "style_values": [
        "#04ed00"
    ]
    *
    *
    * "id_elements": [
        "#link-Anna-27"
    ],
    "style_names": [
        "stroke"
    ],
    "style_values": [
        "#04ed00"
    ]
    * */
    public static String replaceTags(String text, String id_elements, String style_names, String style_values) {
        String compose = "";
        String tmp = "";
        boolean found = false;
        char tag = id_elements.charAt(0);
        id_elements = id_elements.substring(1);

        Log.i("REPLACE STR", id_elements + "---" + style_names + "---" + style_values);

        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.i("STR_LINE", line);

                tmp += "\n" + line;

                switch (tag) {
                    case '#':
                        if (line.contains("id=\"" + id_elements + "\"")) {
                            found = true;
                        }
                        break;
                    case '.':
                        if (line.contains("class=\"" + id_elements + "\"")) {
                            found = true;
                        }
                        break;
                }
                if (line.contains("<") || line.contains(">")) {
                    if (found) {
                        found = false;
                        tmp = substituteStr(tmp, style_names, style_values);
                    }

                    compose += tmp;
                    tmp = "";
                }

            }
        } catch (IOException ignored) {}

        return compose.substring(1);
    }

    private static String substituteStr(String text, String style_names, String style_values) {
        Log.i("STR_SUB", text);
        String compose = "";

        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("style=\"")) {
                    compose += "\n" + line.substring(0, line.length() - 1) + ";" + style_names + ":" + style_values + "\"";
                } else {
                    compose += "\n" + line;
                }
            }
        } catch (IOException ignored) {}

        return compose.substring(1);
    }


    public static String replaceTagsMulti( String svgContent, List<String> idElements, List<String> styleNames, List<String> styleValues) {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < idElements.size(); i++) {
            svgContent = replaceTags(svgContent, idElements.get(i), styleNames.get(i), styleValues.get(i));
        }

        long endTime = System.currentTimeMillis();
        //Log.i("TIME_MODIFY", String.valueOf(endTime - startTime));
        return svgContent;
    }
}
