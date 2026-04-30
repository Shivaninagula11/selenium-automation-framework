package com.automationframework.pages;

import com.automationframework.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage - Page Object Model for the Login Page
 *
 * WHY PAGE OBJECT MODEL?
 * If the login button ID changes tomorrow, you update it in ONE place here.
 * Without POM, you'd search through 50 test files to find every reference.
 *
 * REAL COMPANY PRACTICE:
 * Every page in the application gets its own Page class.
 * Page classes contain ONLY locators and actions — never assertions.
 * Assertions belong in the test classes.
 *
 * We use @FindBy with PageFactory — the industry standard approach.
 * It's cleaner than By.id() scattered everywhere in your tests.
 */
public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // ─── PAGE LOCATORS ───────────────────────────────────────────────────────
    // @FindBy is cleaner than driver.findElement(By.id("...")) in test code
    // All locators are in ONE place — easy to maintain

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "submit")
    private WebElement loginButton;

    @FindBy(id = "error")
    private WebElement errorMessage;

    @FindBy(css = ".post-title")
    private WebElement successPageTitle;

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────

    /**
     * Constructor initializes PageFactory
     * PageFactory.initElements() links @FindBy annotations to actual elements
     * Must be called before any element interaction
     */
    public LoginPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    // ─── PAGE ACTIONS ─────────────────────────────────────────────────────────
    // Methods represent USER ACTIONS on this page
    // Each method does ONE thing and returns the right page object

    /**
     * Enters username in the username field
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        return this; // Return this for method chaining
    }

    /**
     * Enters password in the password field
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
        return this; // Return this for method chaining
    }

    /**
     * Clicks the login button
     * Returns LoginPage because login could fail and stay on same page
     */
    public LoginPage clickLogin() {
        logger.info("Clicking login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        return this;
    }

    /**
     * Complete login flow in one method
     * Uses method chaining for clean, readable code
     *
     * Usage: loginPage.login("student", "Password123")
     */
    public LoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    // ─── PAGE VERIFICATIONS ───────────────────────────────────────────────────
    // These methods return data that tests use for assertions
    // Page class gets the data — test class does the assertion

    /**
     * Returns the error message text when login fails
     */
    public String getErrorMessage() {
        logger.info("Getting error message text");
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }

    /**
     * Returns the success page title text after successful login
     */
    public String getSuccessPageTitle() {
        logger.info("Getting success page title");
        wait.until(ExpectedConditions.visibilityOf(successPageTitle));
        return successPageTitle.getText();
    }

    /**
     * Checks if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if login was successful by verifying URL contains "logged-in"
     */
    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("logged-in"));
            logger.info("Login successful - URL contains 'logged-in'");
            return true;
        } catch (Exception e) {
            logger.warn("Login was not successful");
            return false;
        }
    }
}