package com.solvd.webtest.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties;

    public static void loadFile(String path) {
        if (path == null)
            return;

        properties = new Properties();

        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            try {
                properties.load(new FileInputStream("src/main/resources/_config.properties"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static String get(String key) {
        if (properties == null || properties.getProperty(key) == null)
            return "";

        return properties.getProperty(key);
    }
}
