package com.orangehrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();
    private static ConfigReader instance;

    private ConfigReader() {
        String configPath = "src/test/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties from: " + configPath, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {

        String envKey = key.toUpperCase().replace('.', '_'); // base.url → BASE_URL
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isEmpty()) {
            return envVal;
        }
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }

        return props.getProperty(key, "");
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getAdminUsername() {
        return get("admin.username");
    }

    public String getAdminPassword() {
        return get("admin.password");
    }

    public String getBrowser() {
        return get("browser");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }

    public String getScreenshotDir() {
        return get("screenshot.dir");
    }

    public String getExtentReportPath() {
        return get("extent.report.path");
    }
}
