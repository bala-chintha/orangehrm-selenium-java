package com.orangehrm.pages.admin;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {

    private static final String PAGE_PATH = "/web/index.php/admin/viewSystemUsers";

    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By usernameFilterInput = By.cssSelector(".oxd-table-filter .oxd-input:not([type='hidden'])");
    private final By tableRows = By.cssSelector(".oxd-table-body [role='row']");
    private final By noRecordsText = By.xpath("//span[contains(@class,'oxd-text') and normalize-space()='No Records Found']");
    private final By deleteConfirmButton = By.xpath("//button[normalize-space()='Yes, Delete']");

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        navigate(PAGE_PATH);
        waitForPageReadyBounded(addButton);
    }

    public void clickAdd() {
        click(addButton);
        waitForUrlContains("saveSystemUser");
    }

    public void searchByUsername(String username) {
        typeInPlainInput(usernameFilterInput, username);
        click(searchButton);
        waitForSearchResults();
    }

    public void resetSearch() {
        click(resetButton);
        waitForSearchResults();
    }

    private void waitForSearchResults() {
        waitForEither(tableRows, noRecordsText);
    }

    public int getRowCount() {
        waitForSearchResults();
        return countElements(tableRows);
    }

    public boolean isNoRecordsFound() {
        return isVisible(noRecordsText);
    }

    public void clickDeleteOnRow(int index) {
        By deleteBtn = By.xpath("(//div[contains(@class,'oxd-table-body')])"
                + "[" + (index + 1) + "]//button[1]");
        click(deleteBtn);
        waitForVisible(deleteConfirmButton);
    }

    public void confirmDelete() {
        click(deleteConfirmButton);
        waitForVisible(noRecordsText);
    }
}
