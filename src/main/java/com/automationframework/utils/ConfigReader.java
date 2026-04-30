package com.automationframework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties file
 * 
 * WHY THIS CLASS EXISTS:
 * Never hardcode URLs, credentials, or settings in test files.
 * This class loads everything from config.properties once,
 * and provides clean getter methods to access each property.
 * 
 * REAL COMPANY PRACTICE:
 * Different config files per environment (dev, staging, prod)
 * and the right one is loaded based on a Maven profile or
 * system property passed at runtime.
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "config.properties";

    // Static block runs ONCE when class is first loaded
    // This ensures the file is read only one time across the entire run
    static {
        loadProperties();
    }

    /**
     * Loads the config.properties file into memory
     * Called once via static block
     */
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            logger.info("Configuration file loaded successfully: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("FATAL: Could not load config file: {}", CONFIG_FILE_PATH);
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Generic method to get any property value by key
     * Used internally by specific getters below
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            logger.warn("Property '{}' not found in config file. Check config.properties.", key);
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    // ─── SPECIFIC GETTERS ────────────────────────────────────────────────────

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getUrl() {
        return getProperty("url");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure"));
    }

    public static String getReportTitle() {
        return getProperty("report.title");
    }

    public static String getReportName() {
        return getProperty("report.name");
    }
}