package com.orangehrm.base;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.PageLoadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final ConfigReader config;
    protected final PageLoadHelper pageLoader;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigReader.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        this.pageLoader = new PageLoadHelper(driver, config.getExplicitWait());
    }

    public void navigate(String path) {
        driver.get(config.getBaseUrl() + path);
        pageLoader.waitForDocumentReady();
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public WebElement waitForPageReady(By targetElement) {
        return pageLoader.waitForPageReady(targetElement);
    }

    public void waitForEither(By locatorA, By locatorB) {
        wait.until(d -> {
            try {
                List<WebElement> a = d.findElements(locatorA);
                if (!a.isEmpty() && a.getFirst().isDisplayed()) return true;
            } catch (StaleElementReferenceException ignored) {
            }
            try {
                List<WebElement> b = d.findElements(locatorB);
                return !b.isEmpty() && b.getFirst().isDisplayed();
            } catch (StaleElementReferenceException ignored) {
            }
            return false;
        });
    }

    public void click(By locator) {
        final int maxRetries = 3;
        int attempt = 0;
        do {
            attempt++;
            try {
                pageLoader.waitForAllOverlaysToDisappear();
                waitForClickable(locator).click();
                return;
            } catch (ElementClickInterceptedException e) {
                if (attempt == maxRetries) {
                    throw new ElementClickInterceptedException(
                            "Element still intercepted after " + maxRetries + " retries. Locator: "
                                    + locator + "\n" + e.getMessage());
                }
                pageLoader.waitForAllOverlaysToDisappear();
            } catch (StaleElementReferenceException e) {
                if (attempt == maxRetries) {
                    throw e;
                }
            }
        } while (attempt < maxRetries);
    }

    public void jsClick(By locator) {
        WebElement el = waitForVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void scrollToElement(By locator) {
        WebElement el = waitForVisible(locator);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);

        pageLoader.waitForSpinnerToDisappear();
    }

    public void type(By locator, String text) {
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void clearAndType(By locator, String text) {
        WebElement el = waitForVisible(locator);
        el.sendKeys(Keys.CONTROL + "a");
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    public boolean isVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public int countElements(By locator) {
        return driver.findElements(locator).size();
    }

    public void selectFromDropdown(int dropdownIndex, String optionText) {
        By dropdownTrigger = By.cssSelector(".oxd-select-text");
        List<WebElement> dropdowns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownTrigger));

        if (dropdownIndex >= dropdowns.size()) {
            throw new RuntimeException("Dropdown index " + dropdownIndex + " out of range. Found " + dropdowns.size() + " on page.");
        }
        dropdowns.get(dropdownIndex).click();

        By optionLocator = By.xpath("//div[@role='option']//span[normalize-space()='" + optionText + "']");
        waitForClickable(optionLocator).click();

        // Wait for dropdown to close — confirms selection was registered
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-select-dropdown")));
    }

    public void scrollAndClick(By locator) {
        pageLoader.waitForAllOverlaysToDisappear();
        WebElement el = waitForVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void typeInAutocomplete(By inputLocator, String text, String matchText) {
        WebElement input = waitForVisible(inputLocator);
        input.clear();
        for (char c : text.toCharArray()) {
            input.sendKeys(String.valueOf(c));
        }
        By dropdownLocator = By.cssSelector(".oxd-autocomplete-dropdown");
        waitForVisible(dropdownLocator);
        By optionLocator = By.xpath(
                "//div[contains(@class,'oxd-autocomplete-option')]"
                        + "//span[contains(text(),'" + matchText + "')]");
        waitForClickable(optionLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dropdownLocator));
    }

    public void typeInPlainInput(By inputLocator, String text) {
        WebElement input = waitForVisible(inputLocator);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        wait.until(d -> {
            String val = input.getAttribute("value");
            return val == null || val.isEmpty();
        });
        input.sendKeys(text);
        wait.until(d -> {
            String val = input.getAttribute("value");
            return val != null && val.equals(text);
        });
    }
}