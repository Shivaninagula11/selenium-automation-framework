package com.automationframework.utils;

import com.automationframework.base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ExtentReportManager - Generates HTML test reports automatically
 *
 * WHY IMPLEMENTS ITestListener?
 * TestNG calls these methods automatically at key moments:
 * - onTestStart()   → when a test begins
 * - onTestSuccess() → when a test passes
 * - onTestFailure() → when a test fails (screenshot captured here)
 * - onFinish()      → when suite ends (report flushed here)
 *
 * You register this listener in testng.xml and it works automatically.
 * No changes needed in test classes — fully transparent.
 *
 * REAL COMPANY PRACTICE:
 * Extent Reports is used by QA teams worldwide.
 * The HTML report is emailed after every CI/CD run.
 */
public class ExtentReportManager implements ITestListener {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);

    // ExtentReports is shared across all tests (one report per run)
    private static ExtentReports extent;

    // ExtentTest is per-thread (one test node per test method)
    // ThreadLocal ensures parallel tests don't mix up their report entries
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // ─── REPORT SETUP ────────────────────────────────────────────────────────

    /**
     * Creates the ExtentReports instance with SparkReporter
     * SparkReporter generates the modern HTML dashboard
     */
    private static synchronized ExtentReports createExtentReports() {
        // SparkReporter — generates the HTML file
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/AutomationReport.html");

        // Report appearance settings
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Selenium Framework - Test Results");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        // ExtentReports — the main engine
        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // System info shown in the report dashboard
        extentReports.setSystemInfo("Framework", "Selenium + TestNG + Maven");
        extentReports.setSystemInfo("Author", "Your Name");
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));

        logger.info("Extent Report initialized");
        return extentReports;
    }

    // ─── ITESTLISTENER METHODS ───────────────────────────────────────────────

    /**
     * Called once when the test suite starts
     * Initialize the report here
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started: {}", context.getName());
        extent = createExtentReports();
    }

    /**
     * Called when each individual test method starts
     * Create a new test node in the report
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        logger.info("Test Started: {}", testName);

        // Create test entry in the report
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test); // Store in ThreadLocal for this thread
        extentTest.get().log(Status.INFO, "Test Started: " + testName);
    }

    /**
     * Called when a test PASSES
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test PASSED: {}", testName);
        extentTest.get().log(Status.PASS, "Test Passed: " + testName);
    }

    /**
     * Called when a test FAILS
     * This is where we capture screenshots automatically
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test FAILED: {}", testName);
        logger.error("Failure reason: {}", result.getThrowable().getMessage());

        extentTest.get().log(Status.FAIL, "Test Failed: " + testName);
        extentTest.get().log(Status.FAIL, result.getThrowable());

        // Capture screenshot and embed in report
        try {
            // Get the WebDriver from the test instance
            Object testInstance = result.getInstance();
            if (testInstance instanceof BaseTest) {
                BaseTest baseTest = (BaseTest) testInstance;

                // Capture as Base64 — embeds directly in HTML (no broken links)
                String base64Screenshot = ScreenshotUtil
                    .captureScreenshotAsBase64(baseTest.getDriver());

                if (base64Screenshot != null) {
                    extentTest.get().addScreenCaptureFromBase64String(
                        base64Screenshot,
                        "Screenshot on Failure - " + testName
                    );
                    logger.info("Screenshot embedded in report for: {}", testName);
                }
            }
        } catch (Exception e) {
            logger.error("Could not capture screenshot for failed test: {}", testName, e);
        }
    }

    /**
     * Called when a test is SKIPPED
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test SKIPPED: {}", testName);
        extentTest.get().log(Status.SKIP, "Test Skipped: " + testName);
    }

    /**
     * Called once when the entire test suite FINISHES
     * CRITICAL: flush() must be called here or the report won't be saved
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Finished: {}", context.getName());
        if (extent != null) {
            extent.flush(); // THIS IS CRITICAL — saves the report to disk
            logger.info("Extent Report saved to: reports/AutomationReport.html");
        }
        // Clean up ThreadLocal to prevent memory leaks
        extentTest.remove();
    }
}