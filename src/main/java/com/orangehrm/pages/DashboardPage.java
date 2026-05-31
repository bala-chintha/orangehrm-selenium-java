package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.components.Sidebar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private static final String PAGE_PATH = "/web/index.php/dashboard/index";

    private final By heading = By.xpath("//h6[normalize-space()='Dashboard']");
    private final By userDropdownTab = By.cssSelector(".oxd-userdropdown-tab");
    private final By userDropdown = By.cssSelector(".oxd-userdropdown");
    private final By logoutMenuItem = By.xpath("//a[normalize-space()='Logout']");

    public final Sidebar sidebar;

    public DashboardPage(WebDriver driver) {
        super(driver);
        this.sidebar = new Sidebar(driver);
    }

    public void goTo() {
        navigate(PAGE_PATH);
        waitForPageReady(heading);
    }

    public String getUserName() {
        return getText(userDropdownTab);
    }

    public boolean isDashboardHeadingVisible() {
        try {
            waitForVisible(heading);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        click(userDropdown);
        click(logoutMenuItem);
        waitForUrlContains("auth/login");
    }
}
