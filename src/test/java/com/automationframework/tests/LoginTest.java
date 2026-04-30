package com.automationframework.tests;

import com.automationframework.base.BaseTest;
import com.automationframework.pages.LoginPage;
import com.automationframework.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Test class for Login functionality
 *
 * WHY EXTENDS BaseTest?
 * BaseTest handles browser setup and teardown automatically.
 * We just focus on writing the actual test logic here.
 *
 * TEST SITE: https://practicetestautomation.com/practice-test-login/
 * Username: student
 * Password: Password123
 *
 * REAL COMPANY PRACTICE:
 * - One test class per page/feature
 * - Each test method tests ONE specific scenario
 * - Test names clearly describe what they verify
 * - Tests are independent — no test depends on another
 */
public class LoginTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    /**
     * TEST 1: Verify successful login with valid credentials
     *
     * Steps:
     * 1. Navigate to login page (done by BaseTest)
     * 2. Enter valid username and password
     * 3. Click login
     * 4. Verify success page title is displayed
     */
    @Test(description = "Verify successful login with valid credentials",
          groups = {"smoke", "regression"})
    public void testSuccessfulLogin() {
        logger.info("TEST STARTED: testSuccessfulLogin");

        // Arrange — get test data from config
        String username = ConfigReader.getUsername();
        String password = ConfigReader.getPassword();

        // Act — perform login using Page Object
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);

        // Assert — verify login was successful
        Assert.assertTrue(
            loginPage.isLoginSuccessful(),
            "Login should be successful with valid credentials"
        );

        String pageTitle = loginPage.getSuccessPageTitle();
        Assert.assertEquals(
            pageTitle,
            "Logged In Successfully",
            "Page title should be 'Logged In Successfully' after login"
        );

        logger.info("TEST PASSED: testSuccessfulLogin");
    }

    /**
     * TEST 2: Verify login fails with invalid username
     *
     * Steps:
     * 1. Navigate to login page (done by BaseTest)
     * 2. Enter invalid username with valid password
     * 3. Click login
     * 4. Verify error message is displayed
     */
    @Test(description = "Verify login fails with invalid username",
          groups = {"regression"})
    public void testLoginWithInvalidUsername() {
        logger.info("TEST STARTED: testLoginWithInvalidUsername");

        // Act — perform login with wrong username
        LoginPage loginPage = new LoginPage();
        loginPage.login("invaliduser", "Password123");

        // Assert — verify error message appears
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid username"
        );

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(
            errorMsg.contains("Your username is invalid!"),
            "Error message should say 'Your username is invalid!' but was: " + errorMsg
        );

        logger.info("TEST PASSED: testLoginWithInvalidUsername");
    }

    /**
     * TEST 3: Verify login fails with invalid password
     *
     * Steps:
     * 1. Navigate to login page (done by BaseTest)
     * 2. Enter valid username with wrong password
     * 3. Click login
     * 4. Verify error message is displayed
     */
    @Test(description = "Verify login fails with invalid password",
          groups = {"regression"})
    public void testLoginWithInvalidPassword() {
        logger.info("TEST STARTED: testLoginWithInvalidPassword");

        // Act — perform login with wrong password
        LoginPage loginPage = new LoginPage();
        loginPage.login("student", "wrongpassword");

        // Assert — verify error message appears
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid password"
        );

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(
            errorMsg.contains("Your password is invalid!"),
            "Error message should say 'Your password is invalid!' but was: " + errorMsg
        );

        logger.info("TEST PASSED: testLoginWithInvalidPassword");
    }

    /**
     * TEST 4: Verify login fails with empty credentials
     *
     * Steps:
     * 1. Navigate to login page (done by BaseTest)
     * 2. Leave username and password empty
     * 3. Click login
     * 4. Verify error message is displayed
     */
    @Test(description = "Verify login fails with empty credentials",
          groups = {"regression"})
    public void testLoginWithEmptyCredentials() {
        logger.info("TEST STARTED: testLoginWithEmptyCredentials");

        // Act — click login without entering credentials
        LoginPage loginPage = new LoginPage();
        loginPage.login("", "");

        // Assert — verify error message appears
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for empty credentials"
        );

        logger.info("TEST PASSED: testLoginWithEmptyCredentials");
    }
}
