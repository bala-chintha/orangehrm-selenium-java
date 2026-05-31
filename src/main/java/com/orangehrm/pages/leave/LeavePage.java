package com.orangehrm.pages.leave;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LeavePage extends BasePage {

    private static final String PAGE_PATH = "/web/index.php/leave/viewMyLeaveList";

    private final By applyLeaveLink = By.xpath("//a[normalize-space()='Apply']");
    private final By leaveTable = By.cssSelector(".oxd-table-body");
    private final By tableRows = By.cssSelector(".oxd-table-body [role='row']");
    private final By noRecordsText = By.xpath("//span[normalize-space()='No Records Found']");
    private final By successToast = By.cssSelector(".oxd-toast--success");
    private final By cancelButtons = By.xpath("//button[normalize-space()='Cancel']");

    public LeavePage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        navigate(PAGE_PATH);
        waitForPageReady(leaveTable);
    }

    public void clickApply() {
        click(applyLeaveLink);
        waitForUrlContains("applyLeave");
    }

    public int getRowCount() {
        waitForVisible(leaveTable);
        return countElements(tableRows);
    }

    public boolean isNoRecordsFound() {
        return isVisible(noRecordsText);
    }

    public void cancelFirstLeave() {
        List<WebElement> buttons = driver.findElements(cancelButtons);
        if (buttons.isEmpty()) {
            throw new RuntimeException("No Cancel buttons found — no pending leaves to cancel.");
        }
        buttons.getFirst().click();
        waitForVisible(successToast);
        waitForInvisible(successToast);
    }

    public String getLeaveStatusOnRow(int index) {
        By statusCell = By.xpath("(//div[contains(@class,'oxd-table-card')])"
                + "[" + (index + 1) + "]//div[@role='cell'][7]");
        return getText(statusCell);
    }

    public void cancelAllLeaves() {
        goTo();

        while (countElements(cancelButtons) > 0) {
            List<WebElement> buttons = driver.findElements(cancelButtons);
            if (buttons.isEmpty()) break;

            buttons.getFirst().click();

            waitForVisible(successToast);
            waitForInvisible(successToast);
            waitForEither(tableRows, noRecordsText);
        }
    }
}
