package com.automationframework.base;

import com.automationframework.utils.ConfigReader;
import com.automationframework.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest - Parent class for all test classes
 *
 * WHY THIS CLASS EXISTS:
 * Every test needs a browser open before it runs and closed after.
 * Instead of copy-pasting setup/teardown in every test class,
 * we put it here ONCE and all test classes just extend BaseTest.
 *
 * @BeforeMethod runs before EACH test method
 * @AfterMethod  runs after  EACH test method
 *
 * REAL COMPANY PRACTICE:
 * BaseTest is standard in every enterprise framework.
 * It's the first thing a senior engineer looks for when
 * reviewing a framework's architecture.
 */
public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    /**
     * Setup method - runs before each test
     *
     * @Parameters("browser") reads browser value from testng.xml
     * @Optional("chrome")    uses chrome as default if not specified
     *
     * This enables CROSS-BROWSER testing — just change the
     * parameter in testng.xml to run on any browser
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        logger.info("========================================");
        logger.info("Setting up test - Browser: {}", browser);
        logger.info("========================================");

        // Initialize the WebDriver for this browser
        DriverFactory.initDriver(browser);

        // Navigate to the application URL
        String url = ConfigReader.getUrl();
        getDriver().get(url);
        logger.info("Navigated to URL: {}", url);
    }

    /**
     * Teardown method - runs after each test (always, even if test fails)
     * alwaysRun = true ensures browser closes even when test throws exception
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down test - closing browser");
        DriverFactory.quitDriver();
        logger.info("========================================");
        logger.info("Test completed - Browser closed");
        logger.info("========================================");
    }

    /**
     * Convenience method so test classes can call getDriver()
     * instead of DriverFactory.getDriver() every time
     *
     * Usage in test class:
     *   getDriver().findElement(By.id("username")).sendKeys("student");
     */
    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
