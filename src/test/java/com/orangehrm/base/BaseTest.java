package com.orangehrm.base;

import com.orangehrm.factory.PageObjectManager;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.PageLoadHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;

@Listeners(com.orangehrm.listeners.TestListener.class)
public class BaseTest {

    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private static final ThreadLocal<PageObjectManager> pomHolder = new ThreadLocal<>();

    protected final ConfigReader config = ConfigReader.getInstance();

    // ─── Driver lifecycle ─────────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void setUpDriver() {
        WebDriver driver = createDriver();
        driverHolder.set(driver);
        pomHolder.set(new PageObjectManager(driver));
    }

    private WebDriver createDriver() {
        String browser = config.getBrowser().toLowerCase().trim();
        boolean headless = config.isHeadless();

        WebDriver driver;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (headless) {
                    ffOpts.addArguments("--headless");
                }
                driver = new FirefoxDriver(ffOpts);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOpts = new EdgeOptions();
                if (headless) {
                    edgeOpts.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOpts);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                    chromeOpts.addArguments("--no-sandbox");
                    chromeOpts.addArguments("--disable-dev-shm-usage");
                }
                chromeOpts.addArguments("--start-maximized");
                chromeOpts.addArguments("--disable-notifications");
                chromeOpts.addArguments("--disable-popup-blocking");
                chromeOpts.setExperimentalOption("excludeSwitches",
                        new String[]{"enable-automation"});
                driver = new ChromeDriver(chromeOpts);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();
        return driver;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        WebDriver driver = driverHolder.get();
        if (driver != null) {
            driver.quit();
        }
        driverHolder.remove();
        pomHolder.remove();
    }

    // ─── Accessors ────────────────────────────────────────────────────────────


    public static WebDriver getDriver() {
        return driverHolder.get();
    }

    protected PageObjectManager pom() {
        return pomHolder.get();
    }

    // ─── Shared login helper ──────────────────────────────────────────────────

    protected void loginAsAdmin() {
        pom().getLoginPage().goTo();
        pom().getLoginPage().login(
                config.getAdminUsername(),
                config.getAdminPassword());

        int timeout = config.getExplicitWait();
        By dashboardHeading = By.xpath("//h6[normalize-space()='Dashboard']");

        WebDriverWait loginWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));


        loginWait.until(ExpectedConditions.urlContains("dashboard"));

        loginWait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading));

        new PageLoadHelper(getDriver(), timeout).waitForPageReady(dashboardHeading);
    }
}
