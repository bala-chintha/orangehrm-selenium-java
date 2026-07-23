package com.orangehrm.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;

public class PageLoadHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private final int timeoutSeconds;

    public static final String ALL_OVERLAYS = ".oxd-loading-spinner, .oxd-form-loader, .--loading";

    public PageLoadHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.timeoutSeconds = timeoutSeconds;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
    }

    public void waitForDocumentReady() {
        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                String state = (String) ((JavascriptExecutor) d).executeScript("return document.readyState");
                return "complete".equals(state);
            } catch (JavascriptException e) {
                return false;
            }
        });
    }

    public void waitForVueAppReady() {
        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                WebElement appRoot = d.findElement(By.id("app"));
                return appRoot != null && appRoot.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void waitForAllOverlaysToDisappear() {
        By overlayLocator = By.cssSelector(ALL_OVERLAYS);
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(overlayLocator));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
        } catch (TimeoutException e) {

        }
    }

    @Deprecated
    public void waitForSpinnerToDisappear() {
        waitForAllOverlaysToDisappear();
    }

    public WebElement waitForElementReady(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForPageReady(By targetElement) {
        waitForDocumentReady();
        waitForVueAppReady();
        waitForAllOverlaysToDisappear();
        return waitForElementReady(targetElement);
    }

    public void waitForPageReady() {
        waitForDocumentReady();
        waitForVueAppReady();
        waitForAllOverlaysToDisappear();
    }

    public WebElement waitForPageReadyBounded(By targetElement) {
        Instant deadline = Instant.now().plusSeconds(timeoutSeconds);
        String stage = "documentReady";
        try {
            waitForDocumentReady();
            checkDeadline(deadline, stage);

            stage = "vueAppReady";
            waitForVueAppReady();
            checkDeadline(deadline, stage);

            stage = "overlaysGone";
            waitForAllOverlaysToDisappear();
            checkDeadline(deadline, stage);

            stage = "elementReady";
            return waitForElementReady(targetElement);
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "waitForPageReadyBounded timed out during stage [" + stage
                            + "] within overall " + timeoutSeconds + "s budget. Locator: " + targetElement, e);
        }
    }

    private void checkDeadline(Instant deadline, String completedStage) {
        if (Instant.now().isAfter(deadline)) {
            throw new TimeoutException(
                    "Overall page-ready deadline exceeded right after stage [" + completedStage + "]");
        }
    }
}