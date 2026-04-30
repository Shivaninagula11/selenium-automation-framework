package com.automationframework.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Captures screenshots on test failure
 *
 * WHY THIS EXISTS:
 * When a test fails in CI/CD at 3am, you need visual proof
 * of what went wrong. Screenshots are attached to reports
 * and stored for debugging.
 *
 * REAL COMPANY PRACTICE:
 * Screenshots are automatically captured on failure via
 * TestNG Listeners — no manual calls needed in test code.
 * We'll wire this into ExtentReportManager in Step 11.
 */
public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "screenshots/";

    // Private constructor — utility class, not meant to be instantiated
    private ScreenshotUtil() {}

    /**
     * Captures a screenshot and saves it to the screenshots folder
     *
     * @param driver   - the current WebDriver instance
     * @param testName - name of the test (used in filename)
     * @return         - absolute path of saved screenshot (for report attachment)
     */
    public static String captureScreenshot(WebDriver driver, String testName) {

        if (driver == null) {
            logger.warn("Cannot capture screenshot — WebDriver is null");
            return null;
        }

        // Generate unique filename with timestamp
        // Format: LoginTest_testSuccessfulLogin_2026-04-29_21-00-01.png
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            // Cast driver to TakesScreenshot interface
            TakesScreenshot ts = (TakesScreenshot) driver;

            // Capture screenshot as a temp file
            File srcFile = ts.getScreenshotAs(OutputType.FILE);

            // Copy to our screenshots directory
            File destFile = new File(filePath);

            // Create screenshots directory if it doesn't exist
            destFile.getParentFile().mkdirs();

            FileUtils.copyFile(srcFile, destFile);

            logger.info("Screenshot saved: {}", filePath);
            return new File(filePath).getAbsolutePath();

        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Captures screenshot as Base64 string
     * Used for embedding screenshots directly into HTML reports
     * (No file needed — image is encoded inside the report HTML)
     *
     * @param driver - the current WebDriver instance
     * @return       - Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            logger.warn("Cannot capture screenshot — WebDriver is null");
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String base64Screenshot = ts.getScreenshotAs(OutputType.BASE64);
            logger.info("Screenshot captured as Base64");
            return base64Screenshot;
        } catch (Exception e) {
            logger.error("Failed to capture Base64 screenshot", e);
            return null;
        }
    }
}