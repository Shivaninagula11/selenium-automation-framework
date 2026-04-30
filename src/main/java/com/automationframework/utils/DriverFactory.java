package com.automationframework.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory - Creates and manages WebDriver instances
 *
 * WHY THREADLOCAL?
 * When tests run in PARALLEL, each thread needs its OWN WebDriver.
 * ThreadLocal ensures every thread gets its own isolated driver instance.
 * Without this, parallel tests would share one browser and crash each other.
 *
 * REAL COMPANY PRACTICE:
 * This exact pattern (ThreadLocal WebDriver) is used at companies like
 * Google, Amazon, and Microsoft for their parallel test suites.
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    // ThreadLocal ensures each parallel thread has its OWN WebDriver instance
    // This is the KEY to safe parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Private constructor — nobody should instantiate this class
    // All methods are static, used like: DriverFactory.getDriver()
    private DriverFactory() {}

    /**
     * Returns the WebDriver for the current thread
     * Always use this to interact with the browser
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Initializes WebDriver based on browser name from config
     * Called in @BeforeMethod in BaseTest
     *
     * @param browserName - "chrome", "firefox", or "edge"
     */
    public static void initDriver(String browserName) {
        WebDriver webDriver;

        logger.info("Initializing WebDriver for browser: {}", browserName);

        switch (browserName.toLowerCase().trim()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                // Uncomment below line to run in headless mode (no browser window)
                // chromeOptions.addArguments("--headless=new");
                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                webDriver = new EdgeDriver(edgeOptions);
                break;

            default:
                logger.error("Browser '{}' is not supported. Use: chrome, firefox, edge", browserName);
                throw new IllegalArgumentException(
                    "Unsupported browser: '" + browserName + "'. Valid options: chrome, firefox, edge"
                );
        }

        // Set timeouts
        webDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        webDriver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(ConfigReader.getPageLoadTimeout())
        );

        // Store this driver in ThreadLocal for the current thread
        driver.set(webDriver);
        logger.info("WebDriver initialized successfully for browser: {}", browserName);
    }

    /**
     * Quits the browser and removes driver from ThreadLocal
     * Called in @AfterMethod in BaseTest
     *
     * WHY REMOVE FROM THREADLOCAL?
     * If you don't remove it, the thread pool reuses threads and
     * the old driver leaks into the next test — a very hard bug to find.
     */
    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            logger.info("Quitting WebDriver for current thread");
            webDriver.quit();
            driver.remove(); // CRITICAL: Always remove after quit
        }
    }
}