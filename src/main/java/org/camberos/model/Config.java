package org.camberos.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private Properties properties;
    private static Config instance;

    private Config() {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getApiKey() {
        return properties.getProperty("api.key");
    }

    public String getCsvPath() {
        return properties.getProperty("CSV_PATH");
    }

    public String getJsonPath() {
        return properties.getProperty("JSON_PATH");
    }

    public String getDateFormat() {
        return properties.getProperty("DATE_FORMAT");
    }
}
