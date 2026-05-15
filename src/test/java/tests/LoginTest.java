package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.Reporter;

import java.time.Duration;

/**
 * Selenium TestNG - OrangeHRM Login Test Suite
 * URL: https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
 *
 * Test Scenarios:
 *   1. TC_001 - Login with valid credentials  → PASS
 *   2. TC_002 - Login with invalid password   → FAIL (expected)
 *   3. TC_003 - Login with empty fields       → FAIL (expected)
 */
public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL =
            "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    private static final String VALID_USERNAME = "Admin";
    private static final String VALID_PASSWORD = "admin123";
    private static final String INVALID_PASSWORD = "wrongPassword@99";

    // ─── Setup ────────────────────────────────────────────────────────────────

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Run headless in CI/GitHub Actions environment
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1280,800");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        Reporter.log("🚀 Browser launched | Test starting...", true);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            Reporter.log("🛑 Browser closed.", true);
        }
    }

    // ─── Helper: Navigate to Login Page ───────────────────────────────────────

    private void navigateToLoginPage() {
        driver.get(BASE_URL);
        Reporter.log("🌐 Navigated to: " + BASE_URL, true);

        // Wait until username field is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        ));
    }

    // ─── Helper: Fill Login Form ───────────────────────────────────────────────

    private void fillLoginForm(String username, String password) {
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));

        usernameField.clear();
        usernameField.sendKeys(username);
        Reporter.log("✏️  Entered username: " + username, true);

        passwordField.clear();
        passwordField.sendKeys(password);
        Reporter.log("✏️  Entered password: [hidden]", true);
    }

    // ─── Helper: Click Login Button ────────────────────────────────────────────

    private void clickLoginButton() {
        WebElement loginButton = driver.findElement(
                By.cssSelector("button[type='submit']")
        );
        loginButton.click();
        Reporter.log("🖱️  Clicked Login button", true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TC_001 — Valid Login (Expected: SUCCESS)
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 1,
            description = "TC_001: Login with valid credentials — should redirect to dashboard")
    public void testValidLogin() {
        Reporter.log("▶ TC_001: Valid Login Test", true);

        navigateToLoginPage();
        fillLoginForm(VALID_USERNAME, VALID_PASSWORD);
        clickLoginButton();

        // Step 1: Wait for URL to change away from login page (most reliable)
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("auth/login")
        ));

        String currentUrl = driver.getCurrentUrl();
        Reporter.log("🔀 Redirected to: " + currentUrl, true);

        Assert.assertFalse(
                currentUrl.contains("auth/login"),
                "❌ Still on login page — login may have failed!"
        );

        // Step 2: Wait for top nav user dropdown — always present after login
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".oxd-userdropdown-tab")
        ));

        Reporter.log("✅ TC_001 PASSED — Logged in successfully. URL: " + currentUrl, true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TC_002 — Invalid Password (Expected: ERROR MESSAGE)
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 2,
            description = "TC_002: Login with invalid password — should show error alert")
    public void testInvalidPasswordLogin() {
        Reporter.log("▶ TC_002: Invalid Password Test", true);

        navigateToLoginPage();
        fillLoginForm(VALID_USERNAME, INVALID_PASSWORD);
        clickLoginButton();

        // Wait for the error alert to appear
        WebElement errorAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".oxd-alert-content-text")
                )
        );

        String errorText = errorAlert.getText();
        Reporter.log("🔔 Error message received: " + errorText, true);

        Assert.assertTrue(
                errorAlert.isDisplayed(),
                "❌ Error alert not shown for invalid credentials!"
        );

        Assert.assertTrue(
                errorText.contains("Invalid credentials"),
                "❌ Unexpected error message: " + errorText
        );

        // Confirm user is still on login page
        Assert.assertTrue(
                driver.getCurrentUrl().contains("auth/login"),
                "❌ User navigated away from login page despite invalid credentials!"
        );

        Reporter.log("✅ TC_002 PASSED — Invalid credentials correctly rejected.", true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TC_003 — Empty Fields (Expected: VALIDATION MESSAGES)
    // ══════════════════════════════════════════════════════════════════════════

    @Test(priority = 3,
            description = "TC_003: Login with empty username and password — should show required field errors")
    public void testEmptyFieldsLogin() {
        Reporter.log("▶ TC_003: Empty Fields Validation Test", true);

        navigateToLoginPage();

        // Submit without entering anything
        clickLoginButton();

        // Wait for the required field error spans
        WebElement requiredError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".oxd-input-field-error-message")
                )
        );

        Assert.assertTrue(
                requiredError.isDisplayed(),
                "❌ Required field error not displayed for empty form submission!"
        );

        String errorMsg = requiredError.getText();
        Reporter.log("🔔 Validation message: " + errorMsg, true);

        Assert.assertEquals(
                errorMsg,
                "Required",
                "❌ Expected 'Required' validation message, but got: " + errorMsg
        );

        Reporter.log("✅ TC_003 PASSED — Empty fields correctly validated.", true);
    }
}